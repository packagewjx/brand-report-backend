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

    /**
     * 统计本系统中的行业信息
     * <p>
     * 本方法会创建新的行业统计信息，从本系统中的数据中进行统计。得到的新统计信息不会自动保存
     *
     * @return 使用本系统统计的行业信息
     */
    IndustryStatistics countStatistics(String industry, Integer year, String period, Integer periodTimeNumber);
}
