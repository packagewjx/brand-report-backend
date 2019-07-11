package io.github.packagewjx.brandreportbackend.service.report;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;

/**
 * 根据内部逻辑，导入数据到BrandReport的接口
 */
public interface BrandReportDataImporter {
    BrandReport importData(BrandReport brandReport);
}
