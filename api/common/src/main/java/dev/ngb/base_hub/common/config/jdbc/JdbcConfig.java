package dev.ngb.base_hub.common.config.jdbc;

import dev.ngb.base_hub.common.constant.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = Constants.BASE_PACKAGE)
public class JdbcConfig {
}
