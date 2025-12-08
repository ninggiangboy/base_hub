package dev.ngb.base_hub.ws_gateway;

import dev.ngb.base_hub.common.constant.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = Constants.BASE_PACKAGE)
public class BaseHubWsGatewayApplication {
    static void main(String[] args) {
        SpringApplication.run(BaseHubWsGatewayApplication.class, args);
    }
}
