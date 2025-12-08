package dev.ngb.base_hub.common.infra.jdbc.repository.organization;

import dev.ngb.base_hub.common.infra.jdbc.entity.organization.OrganizationEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationJdbcRepository extends ListCrudRepository<OrganizationEntity, Long> {
    @Override
    @NonNull
    @Query("SELECT * FROM organizations WHERE deleted_at IS NULL")
    List<OrganizationEntity> findAll();

    @Override
    @NonNull
    @Query("SELECT * FROM organizations WHERE id = :id AND deleted_at IS NULL")
    Optional<OrganizationEntity> findById(@NonNull Long id);

    @Override
    @NonNull
    @Query("SELECT * FROM organizations WHERE id in (:ids) AND deleted_at IS NULL")
    List<OrganizationEntity> findAllById(@NonNull Iterable<Long> ids);

    @Override
    @Query("SELECT COUNT(*) FROM organizations WHERE id = :id AND deleted_at IS NULL")
    boolean existsById(Long id);

    @Query("SELECT * FROM organizations WHERE code = :code AND deleted_at IS NULL")
    Optional<OrganizationEntity> findByCode(String code);
}
