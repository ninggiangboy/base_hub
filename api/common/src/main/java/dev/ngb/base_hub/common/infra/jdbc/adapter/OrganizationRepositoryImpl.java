package dev.ngb.base_hub.common.infra.jdbc.adapter;

import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.domain.organization.model.Organization;
import dev.ngb.base_hub.domain.organization.repository.OrganizationRepository;
import dev.ngb.base_hub.common.infra.jdbc.base.BaseEntityJdbcRepository;
import dev.ngb.base_hub.common.infra.jdbc.entity.organization.OrganizationEntity;
import dev.ngb.base_hub.common.infra.jdbc.repository.organization.OrganizationJdbcRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.data.relational.core.query.Criteria;

import java.util.Optional;

import static org.springframework.data.relational.core.query.Criteria.where;

@InfraService
public class OrganizationRepositoryImpl extends BaseEntityJdbcRepository<Organization, OrganizationEntity, Long>
        implements OrganizationRepository {

    private final OrganizationJdbcRepository jdbcRepo;

    public OrganizationRepositoryImpl(OrganizationJdbcRepository jdbcRepo) {
        super(OrganizationEntity.class);
        this.jdbcRepo = jdbcRepo;
    }

    @Override
    protected Organization mapToDomain(@NonNull OrganizationEntity jdbcEntity) {
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

    @Override
    public Optional<Organization> findByCode(String code) {
        Criteria findByCodeCriteria = where("code").is(code);
        return findOneBy(findByCodeCriteria);
    }
}
