package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public interface BrandReportService extends BaseService<BrandReport, String> {
    /**
     * 构建新的报告，根据brandId和时间参数获取数据，执行必要的计算，并返回
     * <p>
     * 构建不会保存报告，也不会尝试获取已有的报告，而只是获取数据并构建
     *
     * @param brandId          品牌Id
     * @param year             年份
     * @param period           统计时长
     * @param periodTimeNumber 年内统计时长序号
     * @return 构建好的报告
     */
    BrandReport buildReport(String brandId, int year, String period, Integer periodTimeNumber);

    /**
     * 构建只有数据的报告。不执行任何统计过程。用于在统计时候使用本类型报告
     *
     * @param brandId          品牌ID
     * @param year             报告年份
     * @param period           统计周期
     * @param periodTimeNumber 年内统计周期序号
     * @return 仅含有原始数据的报告
     */
    BrandReport buildDataOnlyReport(String brandId, int year, String period, Integer periodTimeNumber);
}
