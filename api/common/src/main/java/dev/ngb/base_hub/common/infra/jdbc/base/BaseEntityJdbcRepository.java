package dev.ngb.base_hub.common.infra.jdbc.base;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.base.domain.DomainEntity;
import dev.ngb.base_hub.common.api.identity.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Slf4j
public abstract class BaseEntityJdbcRepository<
        D extends DomainEntity<ID>, J, R extends ListCrudRepository<J, ID>, ID>
        implements BaseDomainRepository<D, ID> {

    protected final R jdbcRepo;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected ObjectMapper objectMapper;

    protected abstract D mapToDomain(J jdbcEntity);

    protected abstract J mapToJdbc(D domainEntity);

    protected String getCurrentUserId() {
        return identityService.getCurrentUserId();
    }

    protected enum Action {
        CREATE, UPDATE, DELETE
    }

    protected void logAudit(Action action, J savingEntity) {
        String jsonEntity = objectMapper.writeValueAsString(savingEntity);
        log.info("[DATABASE AUDIT] action={}, user={}, entity={}", action, getCurrentUserId(), jsonEntity);
    }

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
        entity.markCreatedBy(getCurrentUserId());
        J saved = jdbcRepo.save(mapToJdbc(entity));
        logAudit(Action.CREATE, saved);
        return mapToDomain(saved);
    }

    @Override
    public List<D> createAll(List<D> entities) {
        String userId = getCurrentUserId();
        entities.forEach(entity -> {
            Assert.isNull(entity.getId(), "New entity should not already have an ID");
            entity.markCreatedBy(userId);
        });
        List<J> saved = jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).toList());
        saved.forEach(j -> logAudit(Action.CREATE, j));
        return saved.stream().map(this::mapToDomain).toList();
    }

    @Override
    public D update(D entity) {
        Assert.notNull(entity.getId(), "Old entity should already have an ID");
        entity.markUpdatedBy(getCurrentUserId());
        J saved = jdbcRepo.save(mapToJdbc(entity));
        logAudit(Action.UPDATE, saved);
        return mapToDomain(saved);
    }

    @Override
    public List<D> updateAll(List<D> entities) {
        String userId = getCurrentUserId();
        for (D entity : entities) {
            Assert.notNull(entity.getId(), "Old entity should already have an ID");
            entity.markUpdatedBy(userId);
        }
        List<J> saved = jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).toList());
        saved.forEach(j -> logAudit(Action.UPDATE, j));
        return saved.stream().map(this::mapToDomain).toList();
    }

    @Override
    public void delete(D entity) {
        Assert.notNull(entity.getId(), "Cannot delete an entity without an ID");
        entity.markDeletedBy(getCurrentUserId());
        J deleted = jdbcRepo.save(mapToJdbc(entity));
        logAudit(Action.DELETE, deleted);
    }

    @Override
    public void deleteAll(List<D> entities) {
        String userId = getCurrentUserId();
        for (D entity : entities) {
            Assert.notNull(entity.getId(), "Cannot delete an entity without an ID");
            entity.markDeletedBy(userId);
        }
        List<J> deleted = jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).toList());
        deleted.forEach(j -> logAudit(Action.DELETE, j));
    }

    @Override
    public Boolean existsById(ID id) {
        return jdbcRepo.existsById(id);
    }
}
