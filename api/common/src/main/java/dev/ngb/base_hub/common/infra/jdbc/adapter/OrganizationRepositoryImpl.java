package dev.ngb.base_hub.common.infra.jdbc.adapter;

import tools.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.base.annotation.Adapter;
import dev.ngb.base_hub.domain.organization.model.Organization;
import dev.ngb.base_hub.domain.organization.repository.OrganizationRepository;
import dev.ngb.base_hub.common.infra.jdbc.base.BaseEntityJdbcRepository;
import dev.ngb.base_hub.common.infra.jdbc.entity.organization.OrganizationEntity;
import dev.ngb.base_hub.common.infra.jdbc.repository.organization.OrganizationJdbcRepository;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

@Adapter
public class OrganizationRepositoryImpl extends BaseEntityJdbcRepository<Organization, OrganizationEntity, OrganizationJdbcRepository, Long>
        implements OrganizationRepository {

    public OrganizationRepositoryImpl(OrganizationJdbcRepository jdbcRepo) {
        super(jdbcRepo);
    }

    @Override
    public Optional<Organization> findByCode(String code) {
        return jdbcRepo.findByCode(code).map(this::mapToDomain);
    }

    @Override
    protected Organization mapToDomain(OrganizationEntity jdbcEntity) {
        return Organization.reconstruct(
                jdbcEntity.id(),
                jdbcEntity.name(),
                jdbcEntity.code(),
                jdbcEntity.domain(),
                jdbcEntity.contact(),
                jdbcEntity.description(),
                jdbcEntity.status(),
                jdbcEntity.configuration(),
                jdbcEntity.version(),
                jdbcEntity.createdById(),
                jdbcEntity.updatedById(),
                jdbcEntity.createdAt(),
                jdbcEntity.updatedAt()
        );
    }

    @Override
    protected OrganizationEntity mapToJdbc(@NonNull Organization domainEntity) {
        return OrganizationEntity.builder()
                .id(domainEntity.getId())
                .name(domainEntity.getName())
                .code(domainEntity.getCode())
                .domain(domainEntity.getDomain())
                .contact(domainEntity.getContact())
                .description(domainEntity.getDescription())
                .status(domainEntity.getStatus())
                .configuration(domainEntity.getConfiguration())
                .version(domainEntity.getVersion())
                .createdById(domainEntity.getCreatedById())
                .updatedById(domainEntity.getUpdatedById())
                .deletedById(domainEntity.getDeletedById())
                .createdAt(domainEntity.getCreatedAt())
                .updatedAt(domainEntity.getUpdatedAt())
                .deletedAt(domainEntity.getDeletedAt())
                .build();
    }
}
