package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.meta.Index;

import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public interface IndexService extends BaseService<Index, String> {
    /**
     * 寻找某个中间rootIndexId的子树的所有叶子节点
     *
     * @param rootIndexId 根IndexId
     * @return 叶子Index
     */
    List<Index> getChildIndices(String rootIndexId);

    /**
     * 获取指标树中的叶子指标
     *
     * @return 叶子指标集合
     */
    List<Index> getAllLeafIndices();

    /**
     * 根据类型获取指标
     *
     * @param type 指标的类型
     * @return 该类型指标的列表
     */
    List<Index> getAllByType(String type);

}
