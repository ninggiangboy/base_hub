package dev.ngb.base_hub.common.infra.impl.migration;

import dev.ngb.base_hub.common.api.migration.MigrationService;
import dev.ngb.base_hub.common.base.annotation.InfraService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;

@InfraService
@RequiredArgsConstructor
public class LiquibaseMigrationService implements MigrationService {

    private final DataSource dataSource;
    private static final String CHANGELOG_PATH = "db/changelog/tenant/db.changelog-master.xml";

    @Override
    public void performOrgMigration(String orgId) {
        try (Connection connection = dataSource.getConnection()) {
            // Check if schema exists
            boolean schemaExists;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT schema_name FROM information_schema.schemata WHERE schema_name = ?")) {
                ps.setString(1, orgId);
                try (ResultSet rs = ps.executeQuery()) {
                    schemaExists = rs.next();
                }
            }

            if (schemaExists) {
                throw new IllegalStateException("Schema " + orgId + " already exists");
            }

            // Create schema
            String sql = "CREATE SCHEMA IF NOT EXISTS ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, orgId);
                ps.execute();
            }
            // Change schema to the tenant's schema
            connection.setSchema(orgId);
            // Create Liquibase Database object
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            // Create Liquibase and run migration
            Liquibase liquibase = new Liquibase(CHANGELOG_PATH,
                    new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update(""); // run migrate
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
