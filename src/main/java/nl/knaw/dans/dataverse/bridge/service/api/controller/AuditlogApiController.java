package nl.knaw.dans.dataverse.bridge.service.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.knaw.dans.dataverse.bridge.service.api.config.BridgeConfEnvironment;
import nl.knaw.dans.dataverse.bridge.service.db.dao.ArchivingAuditlogDao;
import nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog;
import nl.knaw.dans.dataverse.bridge.service.generated.api.AuditlogApi;
import nl.knaw.dans.dataverse.bridge.service.generated.model.Error;
import nl.knaw.dans.dataverse.bridge.service.util.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-31T22:37:43.418+02:00")

@Controller
public class AuditlogApiController implements AuditlogApi {

    @Autowired
    private Environment env;

    @Autowired
    private BridgeConfEnvironment bcenv;

    @Autowired
    private SimpleEmail simpleEmail;

    @Autowired
    ArchivingAuditlogDao archivingAuditlogDao;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AuditlogApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
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
    @ApiOperation(value = "Delete all records", nickname = "deleteAll", notes = "Delete all existing records", tags={ "Archiving Auditlog", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record is deleted"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Record not found") })
    @RequestMapping(value = "/auditlog/delete-all",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAll(@ApiParam(value = "" ,required=true) @RequestHeader(value="api_key", required=true) String apiKey) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if(!apiKey.equals( env.getProperty("bridge.apikey")))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            int deletedResult = archivingAuditlogDao.deleteAll();
            log.info("Deleted result: {}", deletedResult);
            simpleEmail.sendToAdmin("DELETE ALL", "All ArchivingAuditlog records are deleted, deletedResult: " + deletedResult);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in public AuditlogApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @ApiOperation(value = "Deletes a record", nickname = "deleteById", notes = "", tags={ "Archiving Auditlog", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record is deleted"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Record not found") })
    @RequestMapping(value = "/auditlog/{id}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@ApiParam(value = "" ,required=true) @RequestHeader(value="api_key", required=true) String apiKey,@ApiParam(value = "Record id to delete",required=true) @PathVariable("id") Long id) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if(!apiKey.equals( env.getProperty("bridge.apikey")))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            ArchivingAuditLog dbArchivingAuditLog = archivingAuditlogDao.getById(id);
            if (dbArchivingAuditLog == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            archivingAuditlogDao.delete(dbArchivingAuditLog);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ArchiveApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @ApiOperation(value = "Delete records filtered by its state", nickname = "deleteFilteredByState", notes = "Delete records filtered by its state", tags={ "Archiving Auditlog", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record is deleted"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Record not found") })
    @RequestMapping(value = "/auditlog/delete/{state}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteFilteredByState(@ApiParam(value = "" ,required=true) @RequestHeader(value="api_key", required=true) String apiKey,@ApiParam(value = "Record id to delete",required=true) @PathVariable("state") String state) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if(!apiKey.equals( env.getProperty("bridge.apikey")))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            int deletedResult = archivingAuditlogDao.deleteFilteredByState(state);
            log.info("Deleted result: {}", deletedResult);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in public AuditlogApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @ApiOperation(value = "Operation to retrive all Archived datasets", nickname = "getAll", notes = "Operation to retrive all Archived datasets", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, responseContainer = "List", tags={ "Archiving Auditlog", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Plugin response", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, responseContainer = "List"),
            @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/auditlog/get-all",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<ArchivingAuditLog>> getAll() {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    List<ArchivingAuditLog> archivingAuditLogs = archivingAuditlogDao.getAll();
                    return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(archivingAuditLogs), List.class), HttpStatus.NOT_IMPLEMENTED);
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


    @ApiOperation(value = "", nickname = "getById", notes = "", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, tags={ "Archiving Auditlog", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Record Id to search", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Record not found") })
    @RequestMapping(value = "/auditlog/{id}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog> getById(@ApiParam(value = "Record id",required=true) @PathVariable("id") Long id) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    ArchivingAuditLog dbArchivingAuditLog = archivingAuditlogDao.getById(id);
                    if (dbArchivingAuditLog == null)
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

                    return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(dbArchivingAuditLog), ArchivingAuditLog.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in public AuditlogApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @ApiOperation(value = "", nickname = "getByState", notes = "", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, responseContainer = "List", tags={ "Archiving Auditlog", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Plugin response", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, responseContainer = "List"),
            @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/auditlog/filtered-by-state/{state}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog>> getByState(@ApiParam(value = "Record id",required=true) @PathVariable("state") String state) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    List<ArchivingAuditLog> archivingAuditLogs = archivingAuditlogDao.getByState(state);
                    return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(archivingAuditLogs), List.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default AuditlogApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
