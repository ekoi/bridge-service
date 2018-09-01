package nl.knaw.dans.dataverse.bridge.service;

import nl.knaw.dans.dataverse.bridge.service.api.config.PluginStorageProperties;
import nl.knaw.dans.dataverse.bridge.service.generated.api.ArchivingApiController;
import nl.knaw.dans.dataverse.bridge.service.generated.api.AuditlogApiController;
import nl.knaw.dans.dataverse.bridge.service.generated.api.DarApiController;
import nl.knaw.dans.dataverse.bridge.service.generated.api.PluginApiController;
import nl.knaw.dans.dataverse.bridge.service.generated.io.swagger.Swagger2SpringBoot;
import nl.knaw.dans.dataverse.bridge.service.generated.io.swagger.config.SwaggerDocumentationConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
    @Author Eko Indarto
 */
@EnableTransactionManagement
@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties({
        PluginStorageProperties.class
})
@ComponentScan(basePackages = { "nl.knaw.dans.dataverse.bridge.service.generated.io.swagger", "nl.knaw.dans.dataverse.bridge.service.generated.api"
        , "nl.knaw.dans.dataverse.bridge.service.api","nl.knaw.dans.dataverse.bridge.service.db", "nl.knaw.dans.dataverse.bridge.service.util"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = {Swagger2SpringBoot.class, SwaggerDocumentationConfig.class, ArchivingApiController.class, PluginApiController.class, DarApiController.class, AuditlogApiController.class}))public class DataverseBridgeService implements CommandLineRunner {

    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(DataverseBridgeService.class).run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
