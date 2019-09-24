package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.repository.statistics.IndustryStatisticsRepository;
import io.github.packagewjx.brandreportbackend.service.BrandReportService;
import io.github.packagewjx.brandreportbackend.service.BrandService;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;
import io.github.packagewjx.brandreportbackend.service.statistics.BrandReportStatisticsCounter;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
@Service
public class IndustryStatisticsServiceImpl extends BaseServiceImpl<IndustryStatistics, String> implements IndustryStatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(IndustryStatisticsServiceImpl.class);
    private BrandService brandService;
    private BrandReportService brandReportService;
    private IndexService indexService;
    private BrandReportStatisticsCounter brandReportStatisticsCounter;


    protected IndustryStatisticsServiceImpl(IndustryStatisticsRepository repository, BrandService brandService, BrandReportService brandReportService, IndexService indexService, BrandReportStatisticsCounter brandReportStatisticsCounter) {
        super(repository);
        this.indexService = indexService;
        this.brandReportStatisticsCounter = brandReportStatisticsCounter;
        this.brandService = brandService;
        this.brandReportService = brandReportService;
    }

    @Override
    @Cacheable("industryStatistics")
    public IndustryStatistics countStatistics(String industry, Integer year, String period, Integer periodTimeNumber) {
        if (year == null || industry == null) {
            throw new IllegalArgumentException("year和industry不能为空");
        }

        logger.info("获取{}行业{}的品牌报告", industry, LogUtils.getLogTime(year, period, periodTimeNumber));
        List<BrandReport> brandReports = getBrandReports(industry, year, period, periodTimeNumber);
        logBrandReport(industry, year, period, periodTimeNumber, brandReports);
        logger.info("获取指标信息中");
        Collection<Index> indices = (Collection<Index>) indexService.getAll();
        // 去掉保存分数的指标
        indices = indices.stream().filter(index ->
                !(index.getAnnotations() != null && index.getAnnotations().containsKey(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR)))
                .collect(Collectors.toSet());
        logger.info("使用品牌报告与指标进行统计");
        IndustryStatistics stat = brandReportStatisticsCounter.count(brandReports, indices);
        stat.setIndustry(industry);
        logger.info("统计完成");
        return stat;
    }

    @Override
    @Cacheable("industryScoreStatistics")
    public IndustryStatistics countScoreStatistics(String industry, Integer year, String period, Integer periodTimeNumber, IndustryStatistics statistics) {
        logger.info("获取存储分数的指标");
        Set<Index> scoreIndex = ((Collection<Index>) indexService.getAll()).stream().filter(index ->
                index.getAnnotations() != null && index.getAnnotations().containsKey(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR))
                .collect(Collectors.toSet());
        logger.debug("共获取到分数指标{}个", scoreIndex.size());
        if (logger.isTraceEnabled()) {
            logger.trace("指标如下");
            scoreIndex.forEach(index ->
                    logger.trace("指标ID为{}，名称为{}，存储ID为{}的分数", index.getIndexId(), index.getDisplayName(),
                            index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR)));
        }

        logger.info("获取{}行业{}的品牌报告", industry, LogUtils.getLogTime(year, period, periodTimeNumber));
        List<BrandReport> brandReports = getBrandReports(industry, year, period, periodTimeNumber);
        logBrandReport(industry, year, period, periodTimeNumber, brandReports);

        IndustryStatistics count = brandReportStatisticsCounter.count(brandReports, scoreIndex);
        if (statistics != null) {
            if (statistics.getStats() == null) {
                statistics.setStats(count.getStats());
            } else {
                statistics.getStats().putAll(count.getStats());
            }
            return statistics;
        } else {
            return count;
        }
    }


    private void logBrandReport(String industry, Integer year, String period, Integer periodTimeNumber, List<BrandReport> brandReports) {
        logger.debug("共获取{}行业{}的品牌报告{}份", industry, LogUtils.getLogTime(year, period, periodTimeNumber), brandReports.size());
        if (logger.isTraceEnabled()) {
            logger.trace("使用了如下的品牌报告");
            brandReports.forEach(brandReport ->
                    logger.trace("{}品牌报告：{}", brandReport.getBrandId(), brandReport.getReportId() == null ? "临时构建报告" : brandReport.getReportId()));
        }
    }

    private List<BrandReport> getBrandReports(String industry, int year, String period, Integer periodTimeNumber) {
        IndustryStatistics stat = new IndustryStatistics();
        stat.setYear(year);
        stat.setPeriod(period);
        stat.setPeriodTimeNumber(periodTimeNumber);
        stat.setIndustry(industry);

        Collection<Brand> industryBrand = brandService.getByIndustry(industry);
        List<BrandReport> brandReports = new ArrayList<>(industryBrand.size());
        // 获取或构建品牌报告，若是构建的报告，则需要删除
        industryBrand.parallelStream().forEach(brand -> {
            BrandReport example = new BrandReport();
            example.setPeriod(period);
            example.setYear(year);
            example.setBrandId(brand.getBrandId());
            example.setPeriodTimeNumber(periodTimeNumber);
            Collection<BrandReport> allByExample = (Collection<BrandReport>) brandReportService.getAllByExample(example);
            if (allByExample.size() == 0) {
                // 构建报告
                logger.info("正在构建用于统计的品牌为{}(ID:{})的临时品牌报告", brand.getBrandName(), brand.getBrandId());
                BrandReport report = brandReportService.buildDataOnlyReport(brand.getBrandId(), year, period, periodTimeNumber);
                brandReports.add(report);
            } else {
                brandReports.add(allByExample.iterator().next());
            }
        });
        return brandReports;
    }

    @Override
    public String getId(IndustryStatistics entity) {
        return entity.getStatId();
    }
}
