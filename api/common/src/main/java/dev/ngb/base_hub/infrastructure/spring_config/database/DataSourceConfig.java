package dev.ngb.base_hub.infrastructure.spring_config.database;

import dev.ngb.base_hub.application.spi.tenant.TenantContextService;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties props, TenantContextService contextService) {
        DataSource ds = props.initializeDataSourceBuilder().build();
        return new SchemaAwareDataSource(ds, contextService);
    }
}