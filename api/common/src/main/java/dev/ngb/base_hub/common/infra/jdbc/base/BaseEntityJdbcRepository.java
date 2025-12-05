package dev.ngb.base_hub.common.infra.jdbc.base;

import dev.ngb.base_hub.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.base.domain.DomainEntity;
import dev.ngb.base_hub.common.api.identity.IdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public abstract class BaseEntityJdbcRepository<
        D extends DomainEntity<ID>, J, R extends ListCrudRepository<J, ID>, ID>
        implements BaseDomainRepository<D, ID> {

    protected final R jdbcRepo;
    protected final IdentityService identityService;

    protected abstract D mapToDomain(J jdbcEntity);

    protected abstract J mapToJdbc(D domainEntity);

    @Override
    public List<D> findAll() {
        return jdbcRepo.findAll().stream().map(this::mapToDomain).toList();
    }

    @Override
    public Optional<D> findById(ID id) {
        return jdbcRepo.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<D> findByIds(List<ID> ids) {
        return jdbcRepo.findAllById(ids).stream().map(this::mapToDomain).toList();
    }

    @Override
    public D create(D entity) {
        Assert.isNull(entity.getId(), "New entity should not already have an ID");
        entity.markCreatedBy(identityService.getCurrentUserId());
        J saved = jdbcRepo.save(mapToJdbc(entity));
        return mapToDomain(saved);
    }

    @Override
    public List<D> createAll(List<D> entities) {
        String userId = identityService.getCurrentUserId();
        entities.forEach(entity -> {
            Assert.isNull(entity.getId(), "New entity should not already have an ID");
            entity.markCreatedBy(userId);
        });
        List<J> saved = jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).toList());
        return saved.stream().map(this::mapToDomain).toList();
    }

    @Override
    public D update(D entity) {
        Assert.notNull(entity.getId(), "Old entity should already have an ID");
        entity.markUpdatedBy(identityService.getCurrentUserId());
        J saved = jdbcRepo.save(mapToJdbc(entity));
        return mapToDomain(saved);
    }

    @Override
    public List<D> updateAll(List<D> entities) {
        String userId = identityService.getCurrentUserId();
        for (D entity : entities) {
            Assert.notNull(entity.getId(), "Old entity should already have an ID");
            entity.markUpdatedBy(userId);
        }
        List<J> saved = jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).toList());
        return saved.stream().map(this::mapToDomain).toList();
    }

    @Override
    public void delete(D entity) {
        Assert.notNull(entity.getId(), "Cannot delete an entity without an ID");
        entity.markDeletedBy(identityService.getCurrentUserId());
        jdbcRepo.save(mapToJdbc(entity));
    }

    @Override
    public void deleteAll(List<D> entities) {
        String userId = identityService.getCurrentUserId();
        for (D entity : entities) {
            Assert.notNull(entity.getId(), "Cannot delete an entity without an ID");
            entity.markDeletedBy(userId);
        }
        jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).toList());
    }

    @Override
    public Boolean existsById(ID id) {
        return jdbcRepo.existsById(id);
    }
}
