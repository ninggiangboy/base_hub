package dev.ngb.base_hub.common.domain.app.model;

import dev.ngb.base_hub.common.base.domain.DomainEntity;
import dev.ngb.base_hub.common.domain.app.constant.AppStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class App extends DomainEntity {
    private String name;
    private String code;
    private String description;
    private AppStatus status;

    public static App create(String name, String code, String description) {
        return App.builder()
                .name(name)
                .code(code)
                .description(description)
                .status(AppStatus.ACTIVE)
                .build();
    }

    public void update(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public void updateStats(AppStatus status) {
        this.status = status;
    }

    public Boolean isActive() {
        return status == AppStatus.ACTIVE;
    }
}
