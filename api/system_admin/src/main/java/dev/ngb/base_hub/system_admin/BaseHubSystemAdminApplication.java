package dev.ngb.base_hub.system_admin;

import dev.ngb.base_hub.base.annotation.AppComponent;
import dev.ngb.base_hub.common.constant.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = Constants.BASE_PACKAGE,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = AppComponent.class
        )
)
public class BaseHubSystemAdminApplication {
    static void main(String[] args) {
        SpringApplication.run(BaseHubSystemAdminApplication.class, args);
    }
}
