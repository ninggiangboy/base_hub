package dev.ngb.base_hub.common.api.migration;

public interface MigrationService {
    void performSchemaMigration(String orgId);

    void performAllSchemasMigration();
}
