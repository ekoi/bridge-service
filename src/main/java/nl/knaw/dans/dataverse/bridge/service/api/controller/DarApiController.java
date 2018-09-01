package nl.knaw.dans.dataverse.bridge.service.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.knaw.dans.dataverse.bridge.service.generated.api.DarApi;
import nl.knaw.dans.dataverse.bridge.service.generated.model.DarIri;
import nl.knaw.dans.dataverse.bridge.service.generated.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-31T22:37:43.418+02:00")

@Controller
public class DarApiController implements DarApi {

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
    @ApiOperation(value = "Operation to create a new DAR IRI", nickname = "addDarIri", notes = "Add a new DAR IRI", tags={ "DAR IRI", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "DarIri succesfully created."),
            @ApiResponse(code = 400, message = "DarIri couldn't have been created."),
            @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/dar",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    public ResponseEntity<Void> addDarIri(@ApiParam(value = "DAR IRI that needs to be added." ,required=true )  @Valid @RequestBody DarIri darNameAndIri) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in public DarApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    @ApiOperation(value = "Operation to retrive all TDR Configuration", nickname = "getAllDarIri", notes = "Operation to retrive all DAR IRI ", response = DarIri.class, responseContainer = "List", tags={ "DAR IRI", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Plugin response", response = DarIri.class, responseContainer = "List"),
            @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/dar/get-all",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<DarIri>> getAllDarIri() {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("[ {  \"iri\" : \"iri\",  \"dar-name\" : \"dar-name\"}, {  \"iri\" : \"iri\",  \"dar-name\" : \"dar-name\"} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in public DarApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
