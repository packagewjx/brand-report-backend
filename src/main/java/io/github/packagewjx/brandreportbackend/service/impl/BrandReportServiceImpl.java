package io.github.packagewjx.brandreportbackend.service.impl;

import com.google.common.collect.Lists;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.repository.BrandReportRepository;
import io.github.packagewjx.brandreportbackend.service.BrandReportService;
import io.github.packagewjx.brandreportbackend.service.report.BrandReportDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.CollectionDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.CompositeDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.ScoreDataImporter;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
@Service
@CacheConfig(cacheNames = {"brandReportService"})
public class BrandReportServiceImpl extends BaseServiceImpl<BrandReport, String> implements BrandReportService {
    private final static Logger logger = LoggerFactory.getLogger(BrandReportServiceImpl.class);

    private final CompositeDataImporter originalDataImporter;

    private final CompositeDataImporter fullDataImporter;

    public BrandReportServiceImpl(BrandReportRepository repository,
                                  ScoreDataImporter scoreDataImporter, CollectionDataImporter collectionDataImporter) {
        super(repository);
        fullDataImporter = new CompositeDataImporter(Lists.newArrayList(collectionDataImporter, scoreDataImporter));
        originalDataImporter = new CompositeDataImporter(Lists.newArrayList(collectionDataImporter));
    }

    @Override
    @Cacheable("brandReportBuild")
    public BrandReport buildReport(String brandId, int year, String period, Integer periodTimeNumber) {
        logger.info("构建品牌Id{}在{}的品牌报告", brandId, LogUtils.getLogTime(year, period, periodTimeNumber));
        return doBuild(brandId, year, period, periodTimeNumber, fullDataImporter);
    }

    @Override
    public BrandReport buildDataOnlyReport(String brandId, int year, String period, Integer periodTimeNumber) {
        logger.info("构建品牌Id{}在{}的原始数据品牌报告", brandId, LogUtils.getLogTime(year, period, periodTimeNumber));
        return doBuild(brandId, year, period, periodTimeNumber, originalDataImporter);
    }

    private BrandReport doBuild(String brandId, int year, String period, Integer periodTimeNumber, BrandReportDataImporter importer) {
        BrandReport report = new BrandReport();
        report.setBrandId(brandId);
        report.setYear(year);
        report.setCreateTime(new Date());
        report.setPeriod(period);
        report.setPeriodTimeNumber(periodTimeNumber);

        // 导入数据
        logger.info("开始给品牌ID{}的品牌报告导入数据", brandId);
        importer.importData(report);
        return report;
    }

    @Override
    public String getId(BrandReport entity) {
        return entity.getReportId();
    }
}
