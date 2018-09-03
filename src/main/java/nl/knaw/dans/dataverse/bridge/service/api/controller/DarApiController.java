package nl.knaw.dans.dataverse.bridge.service.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.knaw.dans.dataverse.bridge.service.api.config.BridgeConfEnvironment;
import nl.knaw.dans.dataverse.bridge.service.generated.api.DarApi;
import nl.knaw.dans.dataverse.bridge.service.generated.model.DarIri;
import nl.knaw.dans.dataverse.bridge.service.generated.model.Error;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-31T22:37:43.418+02:00")

@Controller
public class DarApiController implements DarApi {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private BridgeConfEnvironment bcenv;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public DarApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
    @ApiOperation(value = "Operation to create a new DAR IRI", nickname = "addDarIri", notes = "Add a new DAR IRI. The existing DAR IRI with the same name will be overwritten.", tags = {"DAR IRI",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "DarIri succesfully created."),
            @ApiResponse(code = 400, message = "DarIri couldn't have been created."),
            @ApiResponse(code = 405, message = "Invalid input")})
    @RequestMapping(value = "/dar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<Void> addDarIri(@ApiParam(value = "DAR IRI that needs to be added.", required = true) @Valid @RequestBody DarIri darNameAndIri) {
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            try {
                bcenv.getDarTarget().put(darNameAndIri.getDarName(), darNameAndIri.getIri());
                String jsonDarIri = objectMapper.writeValueAsString(darNameAndIri);
                FileUtils.writeStringToFile(new File(System.getProperty("user.dir")
                        + File.separator + "dar-target-conf"
                        + File.separator + darNameAndIri.getDarName().toLowerCase()
                        + "-" + activeProfile + ".json"), jsonDarIri);
            } catch (JsonProcessingException e) {
                log.error("Couldn't serialize response for content type application/json", e);
            } catch (IOException e) {
                log.error("Cannot write file, msg: {}", e.getMessage());
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default DarApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @ApiOperation(value = "Operation to retrive all TDR Configuration", nickname = "getAllDarIri", notes = "Operation to retrive all DAR IRI ", response = DarIri.class, responseContainer = "List", tags = {"DAR IRI",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Plugin response", response = DarIri.class, responseContainer = "List"),
            @ApiResponse(code = 200, message = "unexpected error", response = Error.class)})
    @RequestMapping(value = "/dar/get-all",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<List<DarIri>> getAllDarIri() {
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    List<DarIri> darIris = new ArrayList<>();
                    bcenv.getDarTarget().forEach((k, v) -> {
                        DarIri darIri = new DarIri();
                        darIri.setDarName(k);
                        darIri.setIri(v);
                        darIris.add(darIri);
                    });
                    return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(darIris), List.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in public DarApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
