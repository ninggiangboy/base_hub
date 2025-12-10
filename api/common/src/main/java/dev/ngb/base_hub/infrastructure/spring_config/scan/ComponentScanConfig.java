package dev.ngb.base_hub.infrastructure.spring_config.scan;

import dev.ngb.base_hub.common.annotation.AppComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = AppComponent.class
        )
)
@Configuration
public class ComponentScanConfig {
}
