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
     * 部分更新实体。仅仅使用updateVal中非null的部分进行更新。
     * 更新使用<code>UtilFunctions.partialChange</code>函数，查看该文档了解详情
     *
     * @param id        ID
     * @param updateVal 新的值
     * @return 更新后的实体
     * @see io.github.packagewjx.brandreportbackend.utils.UtilFunctions
     */
    T partialUpdate(ID id, T updateVal);

    /**
     * 保存所有的实体
     *
     * @param val 实体集
     * @return 保存后的实体集，若ID为空则会添加ID
     */
    Iterable<T> saveAll(Iterable<T> val);

    /**
     * 删除val
     *
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
     * 根据example中非null的字段，查询符合的所有实体
     *
     * @param example 查询条件
     * @return 符合example条件的所有实体
     */
    Iterable<T> getAllByExample(T example);

    /**
     * 查看是否存在
     *
     * @param id ID
     * @return 若存在则为true，否则为false
     */
    boolean existById(ID id);

    /**
     * 获取所有对象
     *
     * @return 所有对象
     */
    Iterable<T> getAll();

    /**
     * 查看id是否是entity的id相等
     *
     * @param id     ID
     * @param entity 实体
     * @return true则是，否则不是
     */
    boolean isIdOfEntity(ID id, T entity);
}
