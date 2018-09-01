package nl.knaw.dans.dataverse.bridge.service.api.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.knaw.dans.dataverse.bridge.plugin.common.DarPluginConf;
import nl.knaw.dans.dataverse.bridge.plugin.common.IAction;
import nl.knaw.dans.dataverse.bridge.plugin.common.IResponseData;
import nl.knaw.dans.dataverse.bridge.plugin.exception.BridgeException;
import nl.knaw.dans.dataverse.bridge.plugin.util.BridgeHelper;
import nl.knaw.dans.dataverse.bridge.plugin.util.StateEnum;
import nl.knaw.dans.dataverse.bridge.service.api.config.BridgeConfEnvironment;
import nl.knaw.dans.dataverse.bridge.service.db.dao.ArchivingAuditlogDao;
import nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog;
import nl.knaw.dans.dataverse.bridge.service.generated.api.ArchivingApi;
import nl.knaw.dans.dataverse.bridge.service.generated.model.Error;
import nl.knaw.dans.dataverse.bridge.service.generated.model.IngestData;
import nl.knaw.dans.dataverse.bridge.service.util.SimpleEmail;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-31T22:37:43.418+02:00")

@Controller
public class ArchivingApiController implements ArchivingApi {
    @Autowired
    private Environment env;

    @Autowired
    private BridgeConfEnvironment bcenv;

    @Autowired
    private SimpleEmail simpleEmail;

    @Autowired
    private ArchivingAuditlogDao archivingAuditlogDao;

    private static final Logger LOG = LoggerFactory.getLogger(ArchivingApiController.class);

    private List<DarPluginConf> darPluginConfList = new ArrayList<DarPluginConf>();

    private static Map<String, String> darTarget = new HashMap<String, String>();

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ArchivingApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        darPluginConfList = bcenv.getPluginList();
        darTarget = bcenv.getDarTarget();
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    @ApiOperation(value = "Operation to retrive a state of an Archived dataset", nickname = "getArchivingState", notes = "Operation to retrive a state of an Archived dataset by filtering pid, version, dar target.", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, tags={ "Archiving", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Plugin response", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class),
            @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/archiving/state",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<ArchivingAuditLog> getArchivingState(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "srcMetadataXml", required = true) String srcMetadataXml, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "srcMetadataVersion", required = true) String srcMetadataVersion, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "targetDarName", required = true) String targetDarName) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    ArchivingAuditLog dbArchivingAuditLog = archivingAuditlogDao.getBySrcxmlSrcversionTargetiri(srcMetadataXml, srcMetadataVersion, targetDarName);
                    if (dbArchivingAuditLog == null) {
                        LOG.error("The following request is NOT FOUND: srcMetadataXml: " + srcMetadataXml + "\tsrcMetadataVersion: " + srcMetadataVersion + "\ttargetDarName: " + targetDarName);
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }

                    return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(dbArchivingAuditLog), ArchivingAuditLog.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ArchiveApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @ApiOperation(value = "Operation to Ingest dataset to DAR", nickname = "ingestToDar", notes = "Ingest dataset to DAR", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, tags={ "Archiving", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Dataset succesfully created.", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class),
            @ApiResponse(code = 400, message = "Dataset couldn't have been created."),
            @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/archiving",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    public ResponseEntity<nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog> ingestToDar(@ApiParam(value = "Dataset object that needs to be added to the Archived's table." ,required=true )  @Valid @RequestBody IngestData ingestData) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    if (!darTarget.containsKey((ingestData.getDarData().getDarName())))
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

                    Optional<DarPluginConf> darPluginConf = darPluginConfList.stream().filter(x -> x.getDarName().equals(ingestData.getDarData().getDarName())).findAny();
                    if (darPluginConf.isPresent()) {
                        String darIri = darTarget.get(ingestData.getDarData().getDarName());
                        int statusCode = checkCredentials(darIri
                                , ingestData.getDarData().getUsername()
                                , ingestData.getDarData().getPassword()
                                , env.getProperty("bridge.dar.timeout", Integer.class));
                        switch (statusCode) {
                            case org.apache.http.HttpStatus.SC_REQUEST_TIMEOUT:
                                return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
                            case org.apache.http.HttpStatus.SC_FORBIDDEN:
                                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                            case org.apache.http.HttpStatus.SC_OK:
                                ArchivingAuditLog dbArchivingAuditLog = archivingAuditlogDao.getBySrcxmlSrcversionTargetiri(ingestData.getSrcData().getSrcXml()
                                        , ingestData.getSrcData().getSrcVersion()
                                        , ingestData.getDarData().getDarName());
                                if (dbArchivingAuditLog != null) {
                                    //existing archived or archiving is in progress
                                    return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(dbArchivingAuditLog), ArchivingAuditLog.class), HttpStatus.OK);
                                }
                                ArchivingAuditLog archivingAuditLog = ingestToDar(ingestData, darIri, darPluginConf.get());
                                return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(archivingAuditLog), ArchivingAuditLog.class), HttpStatus.CREATED);

                        }
                    }
                } catch (URISyntaxException e) {
                    log.error("URISyntaxException: " + e.getMessage());
                } catch (JsonParseException e) {
                    log.error("Couldn't serialize response for content type application/json", e);;
                } catch (JsonMappingException e) {
                    log.error("JsonMappingException: " + e.getMessage());
                } catch (JsonProcessingException e) {
                    log.error("JsonProcessingException: " + e.getMessage());
                } catch (IOException e) {
                    log.error("IOException: " + e.getMessage());
                }
                catch (IllegalAccessException e) {
                    log.error("IllegalAccessException: " + e.getMessage());
                } catch (InstantiationException e) {
                    log.error("InstantiationException: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    log.error("ClassNotFoundException: " + e.getMessage());
                    //send mail
                }

            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ArchiveApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @ApiOperation(value = "Re-ingest dataset", nickname = "reIngestToDar", notes = "Re-ingest the existing dataset. This only can be done for an archive that has IN-PROGRESS state.", tags={ "Archiving", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dataset is archived"),
            @ApiResponse(code = 400, message = "Invalid id supplied"),
            @ApiResponse(code = 404, message = "Archived not found") })
    @RequestMapping(value = "/archiving",
            produces = { "application/json" },
            method = RequestMethod.PUT)
    public ResponseEntity<Void> reIngestToDar(@ApiParam(value = "Updated archive object" ,required=true )  @Valid @RequestBody nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog archivingAuditlog) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            ArchivingAuditLog dbArchivingAuditlog = archivingAuditlogDao.getBySrcxmlSrcversionTargetiriState(archivingAuditlog.getSrcMetadataXml()
                                                                                            , archivingAuditlog.getSrcMetadataVersion()
                                                                                            , archivingAuditlog.getTargetIri(),
                                                                                            "IN-PROGRESS");
            if (dbArchivingAuditlog == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


            log.warn("ObjectMapper or HttpServletRequest not configured in default ArchivingApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    private int checkCredentials(String darIri, String uid, String pwd, int timeout) throws URISyntaxException {
        //check TDR credentials
        //see https://stackoverflow.com/questions/21574478/what-is-the-difference-between-closeablehttpclient-and-httpclient-in-apache-http
        try(CloseableHttpClient httpClient = BridgeHelper.createHttpClient((new IRI(darIri).toURI()), uid, pwd, timeout)){
            HttpGet httpGet = new HttpGet(darIri);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            return org.apache.http.HttpStatus.SC_REQUEST_TIMEOUT;
        }
    }

    private ArchivingAuditLog createNewArchived(IngestData ingestData, String darIri) {
        ArchivingAuditLog archivingAuditLog = new ArchivingAuditLog();
        archivingAuditLog.setStartTime(new Date());
        archivingAuditLog.setSrcMetadataXml(ingestData.getSrcData().getSrcXml());
        archivingAuditLog.setSrcMetadataVersion(ingestData.getSrcData().getSrcVersion());
        archivingAuditLog.setTargetIri(darIri);
        archivingAuditLog.setDarName(ingestData.getDarData().getDarName());
        archivingAuditLog.setState(StateEnum.IN_PROGRESS.toString());
        archivingAuditlogDao.create(archivingAuditLog);
        return archivingAuditLog;
    }

    private ArchivingAuditLog ingestToDar(IngestData ingestData, String darIri, DarPluginConf darPluginConf) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        LOG.info(">>>>>>> Trying ingest to " + ingestData.getDarData().getDarName() + " from url source: " + ingestData.getSrcData().getSrcXml());
        URLClassLoader actionClassLoader = darPluginConf.getActionClassLoader();
        Class actionClass = Class.forName(darPluginConf.getActionClassName(), true, actionClassLoader);
        IAction action = (IAction)actionClass.newInstance();
        LOG.info(action.toString());
        ArchivingAuditLog archivingAuditLog = createNewArchived(ingestData, darIri);
        Flowable.fromCallable(() -> {
            LOG.info("Starting process of ingest using Flowable.fromCallable()");
            Instant start = Instant.now();
            String bagDir = env.getProperty("bridge.temp.dir.bags");
            Map<String, String> transformResult = action.transform(ingestData.getSrcData().getSrcXml(), ingestData.getSrcData().getApiToken(), darPluginConf.getXsl());
            Optional<File> bagitFile = action.composeBagit(bagDir, ingestData.getSrcData().getApiToken(), ingestData.getSrcData().getSrcXml(), transformResult);
            if(bagitFile.isPresent()){
                LOG.info("Set the bagit dir in database. Bagit dir: " + bagDir);
                archivingAuditLog.setBagitDir(bagitFile.get().getAbsolutePath().replace(".zip", ""));
                archivingAuditlogDao.update(archivingAuditLog);
            }
            IResponseData responseDataHolder = action.execute(bagitFile, new IRI(darIri), ingestData.getDarData().getUsername(), Optional.of(ingestData.getDarData().getPassword()));
            if (responseDataHolder != null) {
                LOG.info("Intermediate saving the response data information.");
                archivingAuditLog.setState(responseDataHolder.getState());
                archivingAuditLog.setLog(responseDataHolder.getFeedXml().get());
                archivingAuditlogDao.update(archivingAuditLog);
            }
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).getSeconds();
            LOG.info("The process is done in " + timeElapsed + " seconds.");
            LOG.info("#### End of ingest process using Flowable.fromCallable() ####");
            return responseDataHolder;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .doOnError(ex -> {
                    String msg="";
                    if (ex instanceof BridgeException) {
                        BridgeException be = (BridgeException) ex;
                        msg = "[" + be.getClassName() + "] " + be.getMessage();

                    } else {
                        msg = ex.getMessage();
                    }
                    LOG.error("[doOnError], msg: " + msg);
                    String prevMsg = archivingAuditLog.getLog();
                    if (prevMsg != null)
                        msg = prevMsg + "|" + msg;
                    simpleEmail.sendToAdmin("[doOnError]", msg);
                    archivingAuditLog.setLog(msg);
                    archivingAuditLog.setState(StateEnum.ERROR.toString());
                    archivingAuditLog.setEndTime(new Date());
                    archivingAuditlogDao.update(archivingAuditLog);
                })
                .subscribe(erd -> {
                    if (erd != null)
                        saveAndClean(archivingAuditLog, erd);
                    else
                        LOG.error("The response data is null.");
                }, throwable -> {
                    LOG.error("[throwable], msg: " + throwable.getCause().getMessage());
                    simpleEmail.sendToAdmin("[throwable]", throwable.getCause().getMessage());
                });
        return archivingAuditLog;
    }

    private void saveAndClean(ArchivingAuditLog archivingAuditLog, IResponseData erd) throws JsonProcessingException {
        if (erd.getLandingPage() != null)
            archivingAuditLog.setLandingPage(erd.getLandingPage().get());
        archivingAuditLog.setPid(erd.getPid().get());
        archivingAuditLog.setEndTime(new Date());
        archivingAuditLog.setState(erd.getState());
        if (erd.getFeedXml() != null)
            archivingAuditLog.setLog(erd.getFeedXml().get());
        LOG.info("Ingest finish. Status " + erd.getState());
        if (erd.getState().equals(StateEnum.ARCHIVED.toString())) {
            //delete bagitdir and its zip.
            LOG.info(archivingAuditLog.getBagitDir());
            File bagDirToDelete = FileUtils.getFile(archivingAuditLog.getBagitDir());
            File bagZipFileToDelete = FileUtils.getFile(archivingAuditLog.getBagitDir() + ".zip");
            boolean bagZipFileIsDeleted = FileUtils.deleteQuietly(bagZipFileToDelete);
            if (bagZipFileIsDeleted) {
                LOG.info("Bagit files are deleted.");
                archivingAuditLog.setBagitDir("DELETED");
            } else {
                LOG.warn(bagDirToDelete.getAbsolutePath() + " is not deleted");
                LOG.warn(bagZipFileToDelete + " is not deleted");
            }
        } else {
            simpleEmail.sendToAdmin(erd.getState(), objectMapper.writeValueAsString(archivingAuditLog));
        }
        archivingAuditlogDao.update(archivingAuditLog);
    }
}
