package io.github.packagewjx.brandreportbackend.service.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;

/**
 * @author Junxian Wu
 * <p>
 * 统计数据服务
 */
public interface StatisticsCounter {
    /**
     * 统计指定行业的数据
     *
     * @param industry         行业名称
     * @param year             年份
     * @param period           统计时长
     * @param periodTimeNumber 年内统计时间
     * @return 统计数据
     */
    IndustryStatistics count(String industry, Integer year, String period, Integer periodTimeNumber);
}
