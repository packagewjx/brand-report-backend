package io.github.packagewjx.brandreportbackend.service.report;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;

import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public class CompositeDataImporter implements BrandReportDataImporter {
    private List<BrandReportDataImporter> importers;

    public CompositeDataImporter(List<BrandReportDataImporter> importers) {
        this.importers = importers;
    }

    @Override
    public BrandReport importData(BrandReport brandReport) {
        importers.forEach(brandReportDataImporter -> brandReportDataImporter.importData(brandReport));
        return brandReport;
    }
}
