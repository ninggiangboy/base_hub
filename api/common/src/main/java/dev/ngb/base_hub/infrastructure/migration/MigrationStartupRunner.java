package dev.ngb.base_hub.infrastructure.migration;

import dev.ngb.base_hub.application.spi.migration.MigrationService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MigrationStartupRunner implements ApplicationRunner {

    private final MigrationService migrationService;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        migrationService.performAllSchemasMigration();
    }
}
