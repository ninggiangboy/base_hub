package dev.ngb.base_hub.common.config.scan;

import dev.ngb.base_hub.base.annotation.AppComponent;
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
