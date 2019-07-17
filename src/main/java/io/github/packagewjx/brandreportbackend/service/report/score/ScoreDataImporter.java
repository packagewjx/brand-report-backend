package io.github.packagewjx.brandreportbackend.service.report.score;


import io.github.packagewjx.brandreportbackend.BrandService;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.exception.EntityNotExistException;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.service.report.BrandReportDataImporter;
import io.github.packagewjx.brandreportbackend.service.statistics.StatisticsCounter;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * 负责计算分数的DataImporter
 *
 * @author Junxian Wu
 */
@Service
public class ScoreDataImporter implements BrandReportDataImporter {
    private final IndustryStatisticsService industryStatisticsService;
    private final BrandService brandService;
    private final StatisticsCounter counter;

    public ScoreDataImporter(IndustryStatisticsService industryStatisticsService, BrandService brandService, StatisticsCounter counter) {
        this.industryStatisticsService = industryStatisticsService;
        this.brandService = brandService;
        this.counter = counter;
    }

    /**
     * 查询或计算行业统计数据
     *
     * @param industry
     * @param period
     * @param year
     * @param month
     * @param quarter
     * @return 行业统计数据，保证非空
     */
    private IndustryStatistics getIndustryStatisticsAndTime(String industry, String period, Integer year, Integer month, Integer quarter) {
        Collection<IndustryStatistics> byIndustry = ((Collection<IndustryStatistics>) industryStatisticsService.getByIndustry(industry));
        IndustryStatistics ret = null;
        if (Constants.PERIOD_MONTHLY.equals(period)) {
            if (month == null) {
                throw new IllegalArgumentException("month不能为空");
            }
            ret = byIndustry.stream()
                    .filter(industryStatistics -> Constants.PERIOD_MONTHLY.equals(industryStatistics.getPeriod()))
                    .filter(industryStatistics -> year.equals(industryStatistics.getYear()) && month.equals(industryStatistics.getMonth()))
                    .findAny().orElse(null);
        } else if (Constants.PERIOD_QUARTERLY.equals(period)) {
            if (quarter == null) {
                throw new IllegalArgumentException("quarter不能为空");
            }
            ret = byIndustry.stream()
                    .filter(industryStatistics -> Constants.PERIOD_QUARTERLY.equals(industryStatistics.getPeriod()))
                    .filter(industryStatistics -> quarter.equals(industryStatistics.getMonth()) && year.equals(industryStatistics.getYear()))
                    .findAny().orElse(null);
        } else {
            // 默认计算一年
            ret = byIndustry.stream()
                    .filter(industryStatistics -> year.equals(industryStatistics.getYear()))
                    .findAny().orElse(null);
        }

        // 若没有保存，则返回计算值
        return ret != null ? ret : counter.count(industry, year, month, quarter, period);
    }


    @Override
    public BrandReport importData(BrandReport brandReport) {
        Optional<Brand> oBrand = brandService.getById(brandReport.getBrandId());
        if (!oBrand.isPresent()) {
            throw new EntityNotExistException("不存在BrandId为" + brandReport.getBrandId() + "的品牌");
        }
        if (brandReport.getYear() == null) {
            throw new IllegalArgumentException("year不能为空");
        }

        String industry = oBrand.get().getIndustry();
        IndustryStatistics industryStatistics = getIndustryStatisticsAndTime(industry, brandReport.getPeriod(),
                brandReport.getYear(), brandReport.getMonth(), brandReport.getQuarter());


        return brandReport;
    }
}
