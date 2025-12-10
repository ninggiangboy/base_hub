package dev.ngb.base_hub.application.spi.migration;

public interface MigrationService {
    void performSchemaMigration(String orgId);

    void performAllSchemasMigration();
}
