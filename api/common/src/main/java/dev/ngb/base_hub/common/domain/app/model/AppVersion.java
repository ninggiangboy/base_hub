package dev.ngb.base_hub.common.domain.app.model;

import dev.ngb.base_hub.common.base.domain.DomainEntity;
import dev.ngb.base_hub.common.domain.app.constant.AppFeature;
import dev.ngb.base_hub.common.domain.app.constant.AppVersionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppVersion extends DomainEntity {
    private String version;
    private UUID appId;
    private String changelog;
    private AppVersionStatus status;
    private List<AppFeature> features;
    private Map<String, Object> metadata;
    private Instant releasedAt;

    private static final Set<AppVersionStatus> ACTIVE_STATUSES =
            EnumSet.of(AppVersionStatus.DEVELOPMENT, AppVersionStatus.PUBLISHED);

    public static AppVersion create(
            String version, App app, String changelog, List<AppFeature> features, Instant releasedAt) {
        return AppVersion.builder()
                .version(version)
                .appId(app.getId())
                .changelog(changelog)
                .status(AppVersionStatus.DRAFT)
                .features(features)
                .metadata(new HashMap<>())
                .releasedAt(releasedAt)
                .build();
    }

    public void update(
            String version,
            String changelog,
            List<AppFeature> features,
            Map<String, Object> metadata,
            Instant releasedAt) {
        this.version = version;
        this.changelog = changelog;
        this.features = features;
        this.metadata = metadata;
        this.releasedAt = releasedAt;
    }

    public void updateStatus(AppVersionStatus status) {
        this.status = status;
    }

    public Boolean isActive() {
        return ACTIVE_STATUSES.contains(status);
    }
}
