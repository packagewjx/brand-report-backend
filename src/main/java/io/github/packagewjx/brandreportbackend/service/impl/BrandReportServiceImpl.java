package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.repository.BrandReportRepository;
import io.github.packagewjx.brandreportbackend.service.BrandReportService;
import io.github.packagewjx.brandreportbackend.service.report.BrandReportDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.CollectionDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.CompositeDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreDataImporter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
@Service
public class BrandReportServiceImpl extends BaseServiceImpl<BrandReport, String> implements BrandReportService {
    private final CompositeDataImporter dataImporter;

    public BrandReportServiceImpl(BrandReportRepository repository,
                                  ScoreDataImporter scoreDataImporter, CollectionDataImporter collectionDataImporter) {
        super(repository);
        List<BrandReportDataImporter> importers = new ArrayList<>();
        importers.add(collectionDataImporter);
        importers.add(scoreDataImporter);
        dataImporter = new CompositeDataImporter(importers);
    }

    @Override
    public BrandReport buildReport(String brandId, int year, String period, Integer periodTimeNumber) {
        BrandReport report = new BrandReport();
        report.setBrandId(brandId);
        report.setYear(year);
        report.setCreateTime(new Date());
        report.setPeriod(period);
        report.setPeriodTimeNumber(periodTimeNumber);

        // 导入数据
        dataImporter.importData(report);
        return report;
    }
}
