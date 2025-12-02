package dev.ngb.base_hub.common.infra.jdbc.base;

import dev.ngb.base_hub.common.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.common.base.domain.DomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public abstract class JdbcBaseEntityRepository<
        D extends DomainEntity<ID>, J, R extends ListCrudRepository<J, ID>, ID>
        implements BaseDomainRepository<D, ID> {

    protected final R jdbcRepo;

    protected abstract D mapToDomain(J jdbcEntity);

    protected abstract J mapToJdbc(D domainEntity);

    @Override
    public List<D> findAll() {
        List<D> result = new ArrayList<>();
        jdbcRepo.findAll().forEach(e -> result.add(mapToDomain(e)));
        return result;
    }

    @Override
    public Optional<D> findById(ID id) {
        return jdbcRepo.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<D> findByIds(List<ID> ids) {
        List<D> result = new ArrayList<>();
        jdbcRepo.findAllById(ids).forEach(e -> result.add(mapToDomain(e)));
        return result;
    }

    @Override
    public D create(D entity) {
        assert entity.getId() == null : "New entity should not already have an ID";
        J saved = jdbcRepo.save(mapToJdbc(entity));
        return mapToDomain(saved);
    }

    @Override
    public List<D> createAll(List<D> entities) {
        entities.forEach(entity -> {
            assert entity.getId() == null : "New entity should not already have an ID";
        });
        List<J> saved = jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).collect(Collectors.toList()));
        return saved.stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public D update(D entity) {
        assert entity.getId() != null : "Old entity should already have an ID";
        J saved = jdbcRepo.save(mapToJdbc(entity));
        return mapToDomain(saved);
    }

    @Override
    public List<D> updateAll(List<D> entities) {
        for (D entity : entities) {
            assert entity.getId() != null : "Old entity should already have an ID";
        }
        List<J> saved = jdbcRepo.saveAll(entities.stream().map(this::mapToJdbc).collect(Collectors.toList()));
        return saved.stream().map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(D entity) {
        assert entity.getId() != null : "Cannot delete an entity without an ID";
        jdbcRepo.deleteById(entity.getId());
    }

    @Override
    public void deleteAll(List<D> entities) {
        for (D entity : entities) {
            assert entity.getId() != null : "Cannot delete an entity without an ID";
        }
        jdbcRepo.deleteAllById(entities.stream().map(D::getId).collect(Collectors.toList()));
    }

    @Override
    public Boolean existsById(ID id) {
        return jdbcRepo.existsById(id);
    }
}
