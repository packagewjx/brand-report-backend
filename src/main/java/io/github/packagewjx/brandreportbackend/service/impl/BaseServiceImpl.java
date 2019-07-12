package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.service.BaseService;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-12
 * <p>
 * 基服务的基本实现
 **/
public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
    private CrudRepository<T, ID> repository;

    protected BaseServiceImpl(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T val) {
        return repository.save(val);
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
    public void deleteAll(Iterable<T> ids) {
        repository.deleteAll(ids);
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
    public Iterable<T> getAll() {
        return repository.findAll();
    }
}
