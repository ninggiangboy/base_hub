package dev.ngb.base_hub.infrastructure.persistence.impl;

import dev.ngb.base_hub.common.annotation.InfraService;
import dev.ngb.base_hub.domain.organization.model.Organization;
import dev.ngb.base_hub.domain.organization.repository.OrganizationRepository;
import dev.ngb.base_hub.infrastructure.persistence.base.BaseEntityJdbcRepository;
import dev.ngb.base_hub.infrastructure.persistence.entity.organization.OrganizationEntity;
import dev.ngb.base_hub.infrastructure.persistence.repository.organization.OrganizationJdbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

import static org.springframework.data.relational.core.query.Criteria.where;

@Repository
public class OrganizationRepositoryImpl
        extends BaseEntityJdbcRepository<Organization, OrganizationEntity, Long>
        implements OrganizationRepository {

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
                Map.of(),
                jdbcEntity.version(),
                jdbcEntity.createdById(),
                jdbcEntity.updatedById(),
                jdbcEntity.createdAt(),
                jdbcEntity.updatedAt()
        );
    }

    @Override
    protected OrganizationEntity mapToJdbc(Organization domainEntity) {
        return OrganizationEntity.builder()
                .id(domainEntity.getId())
                .name(domainEntity.getName())
                .code(domainEntity.getCode())
                .domain(domainEntity.getDomain())
                .contact(domainEntity.getContact())
                .description(domainEntity.getDescription())
                .status(domainEntity.getStatus())
//                .configuration(domainEntity.getConfiguration())
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
