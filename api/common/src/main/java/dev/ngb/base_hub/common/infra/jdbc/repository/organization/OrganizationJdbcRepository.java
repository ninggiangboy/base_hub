package dev.ngb.base_hub.common.infra.jdbc.repository.organization;

import dev.ngb.base_hub.common.infra.jdbc.entity.organization.OrganizationEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationJdbcRepository extends ListCrudRepository<OrganizationEntity, Long> {
    @Override
    @Nonnull
    @Query("SELECT * FROM organizations WHERE deleted_at is null")
    List<OrganizationEntity> findAll();

    @Override
    @Nonnull
    @Query("SELECT * FROM organizations WHERE id = :id and deleted_at is null")
    Optional<OrganizationEntity> findById(@Nonnull Long id);

    @Override
    @Nonnull
    @Query("SELECT * FROM organizations WHERE id in (:ids) and deleted_at is null")
    List<OrganizationEntity> findAllById(@Nonnull Iterable<Long> ids);

    @Query("SELECT * FROM organizations WHERE code = :code and deleted_at is null")
    Optional<OrganizationEntity> findByCode(String code);
}
