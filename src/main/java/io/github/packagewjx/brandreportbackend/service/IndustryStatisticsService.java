package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;

import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public interface IndustryStatisticsService extends BaseService<IndustryStatistics, String> {
    /**
     * 根据行业获取行业统计
     *
     * @param industry 行业
     * @return 行业统计数据
     */
    List<IndustryStatistics> getByIndustry(String industry);
}
