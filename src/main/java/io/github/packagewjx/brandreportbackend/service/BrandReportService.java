package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;

import java.util.Collection;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public interface BrandReportService extends BaseService<BrandReport, String> {
    /**
     * 根据品牌ID获取品牌报告
     *
     * @param brandId 品牌ID
     * @return 所有品牌报告
     */
    Collection<BrandReport> getByBrandId(String brandId);

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
}
