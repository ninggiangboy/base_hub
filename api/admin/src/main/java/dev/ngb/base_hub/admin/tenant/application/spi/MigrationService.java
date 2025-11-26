package dev.ngb.base_hub.admin.tenant.application.spi;

public interface MigrationService {
    void performTenantMigration(String tenantId);
}