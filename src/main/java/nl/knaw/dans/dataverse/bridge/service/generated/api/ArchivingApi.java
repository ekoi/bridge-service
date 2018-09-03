/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package nl.knaw.dans.dataverse.bridge.service.generated.api;

import nl.knaw.dans.dataverse.bridge.service.generated.model.Error;
import nl.knaw.dans.dataverse.bridge.service.generated.model.IngestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-09-03T13:13:10.962+02:00")

@Api(value = "archiving", description = "the archiving API")
public interface ArchivingApi {

    Logger log = LoggerFactory.getLogger(ArchivingApi.class);

    default Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    @ApiOperation(value = "Operation to retrive a state of an Archived dataset", nickname = "getArchivingState", notes = "Operation to retrive a state of an archiving dataset by filtering pid, version, dar target.", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, tags={ "Archiving", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Plugin response", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class),
        @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/archiving/state",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog> getArchivingState(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "srcMetadataXml", required = true) String srcMetadataXml,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "srcMetadataVersion", required = true) String srcMetadataVersion,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "targetDarName", required = true) String targetDarName) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{  \"srcXml\" : \"srcXml\",  \"landingPage\" : \"landingPage\",  \"pid\" : \"pid\",  \"startTime\" : \"2000-01-23\",  \"id\" : 0,  \"endTime\" : \"2000-01-23\",  \"state\" : \"IN-PROGRESS\",  \"srcVersion\" : \"srcVersion\",  \"targetIri\" : \"targetIri\"}", nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ArchivingApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Operation to Ingest dataset to DAR", nickname = "ingestToDar", notes = "Ingest dataset to DAR", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class, tags={ "Archiving", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Dataset succesfully created.", response = nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class),
        @ApiResponse(code = 400, message = "Dataset couldn't have been created."),
        @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/archiving",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    default ResponseEntity<nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog> ingestToDar(@ApiParam(value = "Dataset object that needs to be added to the Archived's table." ,required=true )  @Valid @RequestBody IngestData ingestData) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{  \"srcXml\" : \"srcXml\",  \"landingPage\" : \"landingPage\",  \"pid\" : \"pid\",  \"startTime\" : \"2000-01-23\",  \"id\" : 0,  \"endTime\" : \"2000-01-23\",  \"state\" : \"IN-PROGRESS\",  \"srcVersion\" : \"srcVersion\",  \"targetIri\" : \"targetIri\"}", nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ArchivingApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
