package dev.ngb.base_hub.system_admin;

import dev.ngb.base_hub.base.annotation.AppComponent;
import dev.ngb.base_hub.common.constant.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = Constants.BASE_PACKAGE)
public class BaseHubSystemAdminApplication {
    static void main(String[] args) {
        SpringApplication.run(BaseHubSystemAdminApplication.class, args);
    }
}
