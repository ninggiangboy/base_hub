package dev.ngb.base_hub.common.config.jdbc;

import dev.ngb.base_hub.common.constant.ApplicationConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = ApplicationConstants.BASE_PACKAGE)
public class JdbcConfig {
}
