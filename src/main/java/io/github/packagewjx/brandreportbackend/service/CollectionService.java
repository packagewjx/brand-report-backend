package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.data.Collection;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public interface CollectionService extends BaseService<Collection, String> {
    /**
     * 根据时间，获取数据，并合并为一个之后再返回。注意，返回的Collection没有ID
     *
     * @param brandId 品牌ID
     * @param year    年份
     * @param period  统计时长
     * @param periodTimeNumber 年内统计时间序号
     * @return 单个Collection，集合了所有指定品牌的指定统计时长的数据
     */
    Collection getCombinedOneByTimeAndBrand(String brandId, String period, Integer year, Integer periodTimeNumber);
}
