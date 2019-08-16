package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.IndustryReport;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
public interface IndustryReportService extends BaseService<IndustryReport, String> {
    /**
     * 构建行业报告。本操作不会保存新报告也不会获取已有行业报告。
     *
     * @param industry         行业名
     * @param year             报告年份
     * @param period           报告统计时长
     * @param periodTimeNumber 年内报告时间序号
     * @return 行业报告。包含该行业的所有品牌报告以及统计信息
     */
    IndustryReport buildIndustryReport(String industry, Integer year, String period, Integer periodTimeNumber);
}
