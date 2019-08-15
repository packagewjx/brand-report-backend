package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.service.BaseService;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-12
 * <p>
 * 基服务的基本实现
 **/
public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
    private MongoRepository<T, ID> repository;

    protected BaseServiceImpl(MongoRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T val) {
        return repository.save(val);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> val) {
        return repository.saveAll(val);
    }

    @Override
    public void delete(T val) {
        repository.delete(val);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll(Iterable<T> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<T> getAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Iterable<T> getAllByExample(T example) {
        return repository.findAll(Example.of(example));
    }

    @Override
    public boolean existById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public Iterable<T> getAll() {
        return repository.findAll();
    }
}
