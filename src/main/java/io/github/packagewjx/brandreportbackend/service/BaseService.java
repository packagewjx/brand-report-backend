package io.github.packagewjx.brandreportbackend.service;

public interface BaseService<T> {
    /**
     * 插入新的数据
     * <p>
     * 若数据有Id字段，则返回的对象中包含其Id
     *
     * @param val 新数据
     * @return 若插入成功，则返回原val。否则返回null
     */
    T insert(T val);

    /**
     * 更新数据
     *
     * @param id  数据的Id
     * @param val 新数据
     * @return 若成功，返回更新后的val
     */
    T update(Object id, T val);

    /**
     * 删除ID为id的数据
     *
     * @param id 数据Id
     */
    void delete(Object id);

    /**
     * 获取ID为id的数据
     *
     * @param id 数据ID
     * @return 若不存在，则返回null。否则返回数据对象
     */
    T get(Object id);
}
