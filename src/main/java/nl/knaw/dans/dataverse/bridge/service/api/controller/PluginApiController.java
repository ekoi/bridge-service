package nl.knaw.dans.dataverse.bridge.service.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.knaw.dans.dataverse.bridge.plugin.common.DarPluginConf;
import nl.knaw.dans.dataverse.bridge.plugin.exception.BridgeException;
import nl.knaw.dans.dataverse.bridge.service.api.config.BridgeConfEnvironment;
import nl.knaw.dans.dataverse.bridge.service.generated.api.PluginApi;
import nl.knaw.dans.dataverse.bridge.service.generated.model.Error;
import nl.knaw.dans.dataverse.bridge.service.generated.model.Plugin;
import nl.knaw.dans.dataverse.bridge.service.util.PluginRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-25T13:23:02.972+02:00")

@Controller
public class PluginApiController implements PluginApi {
    @Autowired
    private BridgeConfEnvironment bcenv;
    
    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public PluginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
    @Autowired
    private PluginRegisterService pluginRegisterService;

    @Override
    @ApiOperation(value = "Uploads plugin", nickname = "uploadPlugin", notes = "Add a new plugin. The existing plugin with the same name will be overwritten.", tags={ "Plugins", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New Plugin succesfully created."),
            @ApiResponse(code = 400, message = "New Plugin couldn't have been created."),
            @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "/plugin/{dar-name}",
            produces = { "application/json" },
            consumes = { "multipart/form-data" },
            method = RequestMethod.POST)
    public ResponseEntity<Void> uploadPlugin(@ApiParam(value = "file detail") @Valid @RequestParam("file") MultipartFile zipPlugin, @ApiParam(value = "",required=true) @PathVariable("dar-name") String darName) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
                try {
                    DarPluginConf tdrPluginConf = pluginRegisterService.storePlugin(zipPlugin.getInputStream(), zipPlugin.getOriginalFilename(), darName);
                    //register plugin
                    bcenv.getPluginList().add(tdrPluginConf);
                    return new ResponseEntity<>(HttpStatus.OK);
                } catch (BridgeException e) {
                    log.error("Error occured on " + e.getClassName() + ", msg: " + e.getMessage());
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default PluginApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @ApiOperation(value = "List of plugins", nickname = "getAllPlugins", notes = "List of plugins", response = Plugin.class, responseContainer = "List", tags={ "Plugins", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of plugins", response = Plugin.class, responseContainer = "List"),
            @ApiResponse(code = 200, message = "unexpected error", response = Error.class) })
    @RequestMapping(value = "/plugin/get-all",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<Plugin>> getAllPlugins() {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    List<DarPluginConf> pluginsList = bcenv.getPluginList();
                    return new ResponseEntity<>(getObjectMapper().get().readValue(objectMapper.writeValueAsString(pluginsList), List.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default PluginApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
