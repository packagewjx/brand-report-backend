package io.github.packagewjx.brandreportbackend.service;

import java.util.Optional;

/**
 * @author Junxian Wu
 * <p>
 * 实体访问服务
 */
public interface BaseService<T, ID> {
    /**
     * 保存给定实体
     * <p>
     * 若实体是新的实体，没有之前的ID，则是插入新的实体，且返回的实体中包含新的ID。
     * 若实体拥有ID，则更新数据库中对应的实体。
     *
     * @param val 新数据
     * @return 若插入成功，则返回原val。否则返回null
     */
    T save(T val);

    /**
     * 删除val
     * @param val 要删除的实体
     */
    void delete(T val);

    /**
     * 删除ID为id的数据
     *
     * @param id 数据Id
     */
    void deleteById(ID id);

    /**
     * 删除ID在ids集合中的所有实体
     *
     * @param ids 需要删除的实体的ID集合
     */
    void deleteAll(Iterable<T> ids);

    /**
     * 获取ID为id的数据
     *
     * @param id 数据ID
     * @return 若不存在，则返回null。否则返回数据对象
     */
    Optional<T> getById(ID id);

    /**
     * 获取所有ID为ids集合中的id的实体
     *
     * @param ids ID集合
     * @return 所有ID在ids集合的实体
     */
    Iterable<T> getAllById(Iterable<ID> ids);

    /**
     * 获取所有对象
     *
     * @return 所有对象
     */
    Iterable<T> getAll();
}
