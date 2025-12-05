package dev.ngb.base_hub.common.config.jdbc;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties props) {
        DataSource ds = props.initializeDataSourceBuilder().build();
        return new SchemaAwareDataSource(ds);
    }
}