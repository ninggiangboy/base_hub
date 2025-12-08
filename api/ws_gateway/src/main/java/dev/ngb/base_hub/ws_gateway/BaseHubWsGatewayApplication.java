package dev.ngb.base_hub.ws_gateway;

import dev.ngb.base_hub.base.annotation.AppComponent;
import dev.ngb.base_hub.common.constant.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = Constants.BASE_PACKAGE)
@ComponentScan(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = AppComponent.class
        )
)
public class BaseHubWsGatewayApplication {
    static void main(String[] args) {
        SpringApplication.run(BaseHubWsGatewayApplication.class, args);
    }
}
