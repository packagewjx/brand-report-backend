package io.github.packagewjx.brandreportbackend.service.statistics;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;

import java.util.Collection;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-9-16
 **/
public interface BrandReportStatisticsCounter {
    /**
     * 使用指定的报告和指标统计行业报告。
     * <p>
     * 注意，本行业报告，不会有industry字段，因为无法从BrandReport中获取该字段，需要填充
     *
     * @param reports 用于统计的各个报告
     * @param indices 用于统计的指标
     * @return 行业报告
     */
    IndustryStatistics count(Collection<BrandReport> reports, Collection<Index> indices);
}
