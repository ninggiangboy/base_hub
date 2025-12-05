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
    @Query("SELECT * FROM organizations WHERE deleted_at IS NULL")
    List<OrganizationEntity> findAll();

    @Override
    @Nonnull
    @Query("SELECT * FROM organizations WHERE id = :id AND deleted_at IS NULL")
    Optional<OrganizationEntity> findById(@Nonnull Long id);

    @Override
    @Nonnull
    @Query("SELECT * FROM organizations WHERE id in (:ids) AND deleted_at IS NULL")
    List<OrganizationEntity> findAllById(@Nonnull Iterable<Long> ids);

    @Override
    @Nonnull
    @Query("SELECT COUNT(*) FROM organizations WHERE id = :id AND deleted_at IS NULL")
    boolean existsById(Long id);

    @Query("SELECT * FROM organizations WHERE code = :code AND deleted_at IS NULL")
    Optional<OrganizationEntity> findByCode(String code);
}
