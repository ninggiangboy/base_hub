package dev.ngb.base_hub.common.config.jdbc;

import dev.ngb.base_hub.common.context.OrganizationContextHolder;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaAwareDataSource extends DelegatingDataSource {

    public SchemaAwareDataSource(DataSource dataSource) {
        super(dataSource);
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
        String tenant = OrganizationContextHolder.getCurrentOrgId();
        if (tenant != null) {
            connection.setSchema(tenant);
        }
    }
}
