package nl.knaw.dans.dataverse.bridge.service.api.config;

import nl.knaw.dans.dataverse.bridge.plugin.common.DarPluginConf;
import nl.knaw.dans.dataverse.bridge.service.util.PluginRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/*
    @author Eko Indarto
    This class is needed since the Spring's autowiring happens too late.

    Note: System.exit convention
    https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html#exit(int)

    System.exit(0) or EXIT_SUCCESS;  ---> Success
    System.exit(1) or EXIT_FAILURE;  ---> Exception
    System.exit(-1) or EXIT_ERROR;   ---> Error
 */
@Configuration
public class BridgeConfEnvironment implements EnvironmentAware {
    private static final Logger LOG = LoggerFactory.getLogger(BridgeConfEnvironment.class);
    private static Environment env;
    private static final List<DarPluginConf> pluginList = new ArrayList<>();
    private static final Map<String, String> darTarget = new HashMap<>();
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Override
    public void setEnvironment(Environment env) {
        BridgeConfEnvironment.env = env;
        if (env.getActiveProfiles().length == 0){
            LOG.error("# NO profile is given. Please start the application with a profile, eg: '-Dspring.profiles.active=dev'");
            System.exit(1);
        }

        LOG.info("###############################################################");
        LOG.info("#        Starting Dataverse Bridge Using '{}' profile.       #", activeProfile);
        checkingRequiredProperties();
        checkingRequiredDirs();
        registerPlugins();
        readTdrConfiguration();
        LOG.info("#                                                             #");
        LOG.info("###############################################################");
    }

    private void checkingRequiredProperties() {
        List<String> requiredProperties = Arrays.asList("bridge.apps.support.email.from", "spring.mail.host", "bridge.apikey", "bridge.temp.dir.bags");
        requiredProperties.stream().forEach(x -> {
            if(env.getProperty(x) == null || env.getProperty(x).isEmpty()){
                LOG.error("'{}' not found in the application-{}.properties.", x, activeProfile);
                System.exit(1);
            }
        });
    }

    public static Map<String, String> getDarTarget() {
        return darTarget;
    }

    public static List<DarPluginConf> getPluginList() {
        return pluginList;
    }

    private void checkingRequiredDirs() {
        //The following folders are required.
        LOG.info("#                Check the required directories               #");
        List<String> requiredDirs = Arrays.asList("database", "config", "plugins", "dar-target-conf", env.getProperty("bridge.temp.dir.bags"));
        requiredDirs.forEach(dir -> {
            Path path = Paths.get(dir);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    LOG.error("[IOException] - [{}], msg: {}", dir, e.getMessage());
                    System.exit(1);
                }
            }else {
                LOG.info("#     '{}' directory found. ", dir);
            }
        });
    }
    private void registerPlugins(){
        LOG.info("#                                                             #");
        LOG.info("#                 Trying to register plugin...                #");
        try {
            //Eko says: of course we should check many thing here, eq: dir exist, files extension, structures, etc!
            //Checking whether the plugins directory exist or not, is already done by checkingRequiredDirs();
            File pluginsBaseDir = new File("plugins");
            File pluginsDir[] = pluginsBaseDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return !pathname.isFile();
                }
            });
            if (pluginsDir != null) {
                for (File darPluginDir : pluginsDir) {
                    DarPluginConf darPluginConf = PluginRegisterService.attachDarPlugin(darPluginDir);
                    if (darPluginConf == null) break;

                    pluginList.add(darPluginConf);
                }
            }

        } catch (MalformedURLException e) {
            LOG.error("Fail to start the Bridge Service.....");
            LOG.error("MalformedURLException, cause by: " + e.getMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            LOG.error("Fail to start the Bridge Service.....");
            LOG.error("plugins directory doesn't exist" );
            LOG.error("FileNotFoundException, cause by: " + e.getMessage());
            System.exit(1);//Yes, the application will terminated!
        }
        if (pluginList.isEmpty()) {
            LOG.warn("No plugin is registered. Adding at least one plugin is required, otherwise you cannot use this application");
            LOG.warn("Adding plugin can be done by uploading the plugins in zip format through bridge api");
        }
    }

    private void readTdrConfiguration(){
        LOG.info("#                                                             #");
        LOG.info("#                Registering TDRs Configuration:              #");
        File darTargetConfBaseDir = new File("dar-target-conf");
        File darTargetConfFiles[] = darTargetConfBaseDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("-" + activeProfile + ".json")) {
                    LOG.info("#      {} DAR Configuration found ", name);
                    return true;
                }
                return false;
            }
        });
        if (darTargetConfFiles != null) {
            for (File darTargetConfFile : darTargetConfFiles) {
                FileInputStream fis;
                try {
                    fis = new FileInputStream(darTargetConfFile);
                    JsonReader reader = Json.createReader(fis);
                    JsonObject darTargetConfJson = reader.readObject();
                    reader.close();
                    darTarget.put(darTargetConfJson.getString("dar-name"), darTargetConfJson.getString("iri"));
                } catch (FileNotFoundException e) {
                    LOG.error("Fail to start the Bridge Service.....");
                    LOG.error("FileNotFoundException, cause by: " + e.getMessage());
                    LOG.error("No file exist: " + darTargetConfFile.getAbsolutePath());
                    System.exit(1);//Yes, the application will terminated!
                }
            }
        }
        if(darTarget.isEmpty()) {
            LOG.warn("No DAR configuration exist. Adding at least one DAR configuration is required, otherwise you cannot use this application");
            LOG.warn("Adding DAR configuration can be done by uploading the DAR configuration in json format through bridge api");
        }
    }

}