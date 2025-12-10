package dev.ngb.base_hub.infrastructure.persistence.base;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import tools.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.domain.base.BaseDomainRepository;
import dev.ngb.base_hub.domain.base.BaseDomainEntity;
import dev.ngb.base_hub.application.spi.security.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Transactional
@RequiredArgsConstructor
@Slf4j
public abstract class BaseEntityJdbcRepository<D extends BaseDomainEntity<ID>, J, ID> implements BaseDomainRepository<D, ID> {

    private final Class<J> entityClass;
    @Autowired
    protected JdbcAggregateTemplate template;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    protected BaseEntityJdbcRepository() {
        this.entityClass = (Class<J>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    protected abstract D mapToDomain(J jdbcEntity);

    protected abstract J mapToJdbc(D domainEntity);

    protected String getCurrentUserId() {
        return identityService.getCurrentUserId();
    }

    protected final Criteria defaultCriteria = where("deleted_at").isNull();

    protected enum Action {
        CREATE, UPDATE, DELETE
    }

    protected void logAudit(Action action, J savingEntity) {
        String jsonEntity = objectMapper.writeValueAsString(savingEntity);
        log.info("[DATABASE AUDIT] action={}, user={}, entity={}", action, getCurrentUserId(), jsonEntity);
    }

    protected List<D> findAllBy(@Nullable Criteria criteria) {
        Criteria finalCriteria = Optional.ofNullable(criteria).map(defaultCriteria::and).orElse(defaultCriteria);
        return template.findAll(query(finalCriteria), entityClass).stream().map(this::mapToDomain).toList();
    }

    protected Optional<D> findOneBy(@Nullable Criteria criteria) {
        Criteria finalCriteria = Optional.ofNullable(criteria).map(defaultCriteria::and).orElse(defaultCriteria);
        return template.findOne(query(finalCriteria), entityClass).map(this::mapToDomain);
    }

    @Override
    public List<D> findAll() {
        return findAllBy(null);
    }

    @Override
    public Optional<D> findById(ID id) {
        Criteria findByIdCriteria = where("id").is(id);
        return findOneBy(findByIdCriteria);
    }

    @Override
    public List<D> findByIds(List<ID> ids) {
        Criteria findByIdsCriteria = where("id").in(ids);
        return findAllBy(findByIdsCriteria);
    }

    @Override
    public boolean existsById(ID id) {
        Criteria findByIdCriteria = where("id").is(id);
        return template.existsById(query(defaultCriteria.and(findByIdCriteria)), entityClass);
    }

    @Override
    public D create(D entity) {
        Assert.isNull(entity.getId(), "New entity should not already have an ID");
        entity.markCreatedBy(getCurrentUserId());
        J saved = template.insert(mapToJdbc(entity));
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
        List<J> saved = template.insertAll(entities.stream().map(this::mapToJdbc).toList());
        saved.forEach(j -> logAudit(Action.CREATE, j));
        return saved.stream().map(this::mapToDomain).toList();
    }

    @Override
    public D update(D entity) {
        Assert.notNull(entity.getId(), "Old entity should already have an ID");
        entity.markUpdatedBy(getCurrentUserId());
        J saved = template.update(mapToJdbc(entity));
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
        List<J> saved = template.updateAll(entities.stream().map(this::mapToJdbc).toList());
        saved.forEach(j -> logAudit(Action.UPDATE, j));
        return saved.stream().map(this::mapToDomain).toList();
    }

    @Override
    public void delete(D entity) {
        Assert.notNull(entity.getId(), "Cannot delete an entity without an ID");
        entity.markDeletedBy(getCurrentUserId());
        J deleted = template.update(mapToJdbc(entity));
        logAudit(Action.DELETE, deleted);
    }

    @Override
    public void deleteAll(List<D> entities) {
        String userId = getCurrentUserId();
        for (D entity : entities) {
            Assert.notNull(entity.getId(), "Cannot delete an entity without an ID");
            entity.markDeletedBy(userId);
        }
        List<J> deleted = template.updateAll(entities.stream().map(this::mapToJdbc).toList());
        deleted.forEach(j -> logAudit(Action.DELETE, j));
    }
}
