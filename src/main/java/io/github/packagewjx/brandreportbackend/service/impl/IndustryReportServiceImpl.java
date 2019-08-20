package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.IndustryReport;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.repository.IndustryReportRepository;
import io.github.packagewjx.brandreportbackend.service.BrandReportService;
import io.github.packagewjx.brandreportbackend.service.BrandService;
import io.github.packagewjx.brandreportbackend.service.IndustryReportService;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
@Service
public class IndustryReportServiceImpl extends BaseServiceImpl<IndustryReport, String> implements IndustryReportService {
    private static Logger logger = LoggerFactory.getLogger(IndustryReportServiceImpl.class);

    private BrandService brandService;

    private IndustryStatisticsService industryStatisticsService;

    private BrandReportService brandReportService;

    private IndustryReportRepository reportRepository;

    protected IndustryReportServiceImpl(IndustryReportRepository repository, BrandService brandService, IndustryStatisticsService industryStatisticsService, BrandReportService brandReportService) {
        super(repository);
        this.brandService = brandService;
        this.industryStatisticsService = industryStatisticsService;
        this.brandReportService = brandReportService;
    }

    @Override
    public IndustryReport buildIndustryReport(String industry, Integer year, @Nullable String period, @Nullable Integer periodTimeNumber) {
        if (industry == null || "".equals(industry) || year == null) {
            throw new IllegalArgumentException("industry和year不能为空");
        }
        if (period == null) {
            period = Constants.PERIOD_ANNUAL;
        }
        if (periodTimeNumber == null) {
            periodTimeNumber = 1;
        }
        logger.info("构建{}行业在{}的行业报告", industry, LogUtils.getLogTime(year, period, periodTimeNumber));

        IndustryReport report = new IndustryReport();
        report.setIndustry(industry);
        report.setYear(year);
        report.setPeriod(period);
        report.setPeriodTimeNumber(periodTimeNumber);
        report.setBrandReports(new ConcurrentHashMap<>(16));
        report.setCreateTime(new Date());

        List<Brand> industryBrands = ((Collection<Brand>) brandService.getAll()).stream()
                .filter(brand -> industry.equals(brand.getIndustry())).collect(Collectors.toList());
        if (industryBrands.size() == 0) {
            logger.info("系统中{}行业没有品牌信息，返回空的行业报告", industry);
            IndustryStatistics statistics = new IndustryStatistics();
            statistics.setStats(Collections.EMPTY_MAP);
            report.setStat(statistics);
            return report;
        }

        logger.info("获取行业统计数据");
        IndustryStatistics queryExample = new IndustryStatistics();
        queryExample.setIndustry(industry);
        Collection<IndustryStatistics> statByIndustry = ((Collection<IndustryStatistics>) industryStatisticsService.getAllByExample(queryExample));
        // 临时变量
        String finalPeriod = period;
        Integer finalPeriodTimeNumber = periodTimeNumber;
        Optional<IndustryStatistics> any = statByIndustry.stream()
                .filter(stat -> year.equals(stat.getYear()))
                .filter(stat -> finalPeriod.equals(stat.getPeriod()))
                .filter(stat -> !Constants.PERIOD_ANNUAL.equals(finalPeriod) && finalPeriodTimeNumber.equals(stat.getPeriodTimeNumber()))
                .findAny();
        IndustryStatistics statistics = any.orElse(industryStatisticsService.countStatistics(industry, year, period, periodTimeNumber));
        report.setStat(statistics);

        industryBrands.parallelStream().forEach(brand -> {
            logger.info("获取{}品牌在{}的品牌报告", brand.getBrandName(), LogUtils.getLogTime(year, finalPeriod, finalPeriodTimeNumber));
            BrandReport brandExample = new BrandReport();
            brandExample.setBrandId(brand.getBrandId());
            Collection<BrandReport> byBrandId = ((Collection<BrandReport>) brandReportService.getAllByExample(brandExample));
            // 目前仅获取其中一份报告，而不管优先级
            Optional<BrandReport> any1 = byBrandId.stream().filter(brandReport -> year.equals(brandReport.getYear()))
                    .filter(brandReport -> finalPeriod.equals(brandReport.getPeriod()))
                    .filter(brandReport -> !Constants.PERIOD_ANNUAL.equals(finalPeriod) && finalPeriodTimeNumber.equals(brandReport.getPeriodTimeNumber()))
                    .findAny();
            if (any1.isPresent()) {
                report.getBrandReports().put(brand.getBrandId(), any1.get());
            } else {
                logger.info("{}品牌在{}的报告不存在，进入构建", brand.getBrandName(), LogUtils.getLogTime(year, finalPeriod, finalPeriodTimeNumber));
                BrandReport brandReport = brandReportService.buildReport(brand.getBrandId(), year, finalPeriod, finalPeriodTimeNumber);
                report.getBrandReports().put(brand.getBrandId(), brandReport);
            }
        });

        return report;
    }

    @Override
    public boolean isIdOfEntity(String s, IndustryReport entity) {
        return Objects.equals(s, entity.getIndustryReportId());
    }
}
