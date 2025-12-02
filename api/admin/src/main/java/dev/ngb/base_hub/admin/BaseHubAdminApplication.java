package dev.ngb.base_hub.admin;

import dev.ngb.base_hub.base.annotation.AppComponent;
import dev.ngb.base_hub.common.constant.ApplicationConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan(
        basePackages = ApplicationConstants.BASE_PACKAGE,
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = AppComponent.class)
)
@EnableJdbcRepositories(basePackages = ApplicationConstants.BASE_PACKAGE)
public class BaseHubAdminApplication {
    static void main(String[] args) {
        SpringApplication.run(BaseHubAdminApplication.class, args);
    }
}
