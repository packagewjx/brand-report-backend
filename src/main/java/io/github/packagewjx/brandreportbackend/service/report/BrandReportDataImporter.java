package io.github.packagewjx.brandreportbackend.service.report;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;

/**
 * 导入具体数据到BrandReport的接口
 * <p>
 * 接口的各个实现，可以使用Index类中annotations字段获取计算时需要的参数
 */
public interface BrandReportDataImporter {
    /**
     * 导入数据到BrandReport中，导入过程与必要的计算和处理
     *
     * @param brandReport 处理的品牌报告
     * @return 处理完毕的品牌报告
     */
    BrandReport importData(BrandReport brandReport);
}
