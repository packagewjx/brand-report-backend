package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.repository.BrandReportRepository;
import io.github.packagewjx.brandreportbackend.service.BrandReportService;
import io.github.packagewjx.brandreportbackend.service.report.BrandReportDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.CollectionDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.CompositeDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreDataImporter;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
@Service
public class BrandReportServiceImpl extends BaseServiceImpl<BrandReport, String> implements BrandReportService {
    private final static Logger logger = LoggerFactory.getLogger(BrandReportServiceImpl.class);

    private final CompositeDataImporter dataImporter;

    private final BrandReportRepository reportRepository;

    public BrandReportServiceImpl(BrandReportRepository repository,
                                  ScoreDataImporter scoreDataImporter, CollectionDataImporter collectionDataImporter) {
        super(repository);
        List<BrandReportDataImporter> importers = new ArrayList<>();
        importers.add(collectionDataImporter);
        importers.add(scoreDataImporter);
        dataImporter = new CompositeDataImporter(importers);
        reportRepository = repository;
    }

    @Override
    public Collection<BrandReport> getByBrandId(String brandId) {
        return reportRepository.findByBrandId(brandId);
    }

    @Override
    public BrandReport buildReport(String brandId, int year, String period, Integer periodTimeNumber) {
        logger.info("构建品牌Id{}在{}的品牌报告", brandId, LogUtils.getLogTime(year, period, periodTimeNumber));

        BrandReport report = new BrandReport();
        report.setBrandId(brandId);
        report.setYear(year);
        report.setCreateTime(new Date());
        report.setPeriod(period);
        report.setPeriodTimeNumber(periodTimeNumber);

        // 导入数据
        logger.info("开始给品牌ID{}的品牌报告导入数据", brandId);
        dataImporter.importData(report);
        return report;
    }
}
