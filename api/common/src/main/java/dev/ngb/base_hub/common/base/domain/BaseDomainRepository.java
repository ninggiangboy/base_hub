package dev.ngb.base_hub.common.base.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseDomainRepository<T extends DomainEntity<ID>, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    List<T> findByIds(List<ID> ids);

    T create(T entity);

    List<T> createAll(List<T> entities);

    T update(T entity);

    List<T> updateAll(List<T> entities);

    void delete(T entity);

    void deleteAll(List<T> entities);

    Boolean existsById(ID id);
}
