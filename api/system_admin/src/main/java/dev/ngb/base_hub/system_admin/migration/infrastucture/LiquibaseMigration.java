package dev.ngb.base_hub.system_admin.migration.infrastucture;

import dev.ngb.base_hub.system_admin.shared.migration.public_api.MigrationPublicApi;
import dev.ngb.base_hub.base.annotation.InfraService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;

@InfraService
@RequiredArgsConstructor
@Slf4j
public class LiquibaseMigration implements MigrationPublicApi {

    private final DataSource dataSource;
    private static final String CHANGELOG_PATH = "db.changelog/schema/db.changelog-master.yaml";

    @Override
    public void performOrgSchemaMigration(String orgId) {
        log.info("Starting schema migration for orgId={}", orgId);

        try (Connection connection = dataSource.getConnection()) {

            boolean schemaExists;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT schema_name FROM information_schema.schemata WHERE schema_name = ?")) {
                ps.setString(1, orgId);
                try (ResultSet rs = ps.executeQuery()) {
                    schemaExists = rs.next();
                }
            }

            if (schemaExists) {
                log.error("Schema {} already exists — aborting migration", orgId);
                throw new IllegalStateException("Schema " + orgId + " already exists");
            }

            log.info("Creating schema {}", orgId);

            // Escape double quotes and wrap in quotes for identifier safety
            String quotedOrgId = "\"" + orgId.replace("\"", "\"\"") + "\"";
            String sql = "CREATE SCHEMA IF NOT EXISTS " + quotedOrgId;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.execute();
            }

            connection.setSchema(orgId);

            log.info("Initializing Liquibase for schema {}", orgId);
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    CHANGELOG_PATH,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            log.info("Running Liquibase migration for schema {}", orgId);
            liquibase.update("");

            log.info("Migration completed successfully for schema {}", orgId);

        } catch (SQLException | LiquibaseException e) {
            log.error("Migration failed for orgId={}", orgId, e);
            throw new RuntimeException(e);
        }
    }
}
