package dev.ngb.base_hub.infrastructure.spring_config.database;

import dev.ngb.base_hub.application.spi.tenant.TenantContextService;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaAwareDataSource extends DelegatingDataSource {

    private final TenantContextService tenantContextService;

    public SchemaAwareDataSource(DataSource dataSource, TenantContextService tenantContextService) {
        super(dataSource);
        this.tenantContextService = tenantContextService;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();
        setSchema(connection);
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = super.getConnection(username, password);
        setSchema(connection);
        return connection;
    }

    private void setSchema(Connection connection) throws SQLException {
        String tenantId = tenantContextService.getTenantId();
        if (tenantId != null) {
            connection.setSchema(tenantId);
        }
    }
}
