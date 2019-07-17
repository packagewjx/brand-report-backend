package io.github.packagewjx.brandreportbackend.service.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;

/**
 * @author Junxian Wu
 * <p>
 * 统计数据服务
 */
public interface Statistics {
    /**
     * 统计指定行业的数据
     *
     * @param industry 行业名称
     * @param year 年份
     * @param month 月份
     * @param quarter 季度
     * @param period 统计时长
     * @return 统计数据
     */
    IndustryStatistics count(String industry, Integer year, Integer month, Integer quarter, String period);
}
