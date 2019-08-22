package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.dataobject.IndustryReportDO;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.IndustryReport;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.exception.EntityNotExistException;
import io.github.packagewjx.brandreportbackend.exception.MethodNotSupportException;
import io.github.packagewjx.brandreportbackend.repository.IndustryReportDORepository;
import io.github.packagewjx.brandreportbackend.service.BrandReportService;
import io.github.packagewjx.brandreportbackend.service.BrandService;
import io.github.packagewjx.brandreportbackend.service.IndustryReportService;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
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
@CacheConfig(cacheNames = {"industryReportService"})
public class IndustryReportServiceImpl implements IndustryReportService {
    private static Logger logger = LoggerFactory.getLogger(IndustryReportServiceImpl.class);
    private BrandService brandService;
    private IndustryStatisticsService industryStatisticsService;
    private BrandReportService brandReportService;
    private IndustryReportDORepository doRepository;

    protected IndustryReportServiceImpl(BrandService brandService, IndustryStatisticsService industryStatisticsService, BrandReportService brandReportService, IndustryReportDORepository doRepository) {
        this.brandService = brandService;
        this.industryStatisticsService = industryStatisticsService;
        this.brandReportService = brandReportService;
        this.doRepository = doRepository;
    }

    /**
     * 转换行业报告BO为DO
     *
     * @param report 行业报告BO
     * @return 行业报告DO
     */
    private IndustryReportDO toDO(@Nullable IndustryReport report) {
        if (report == null) {
            return null;
        }
        IndustryReportDO reportDO = new IndustryReportDO();
        reportDO.setIndustryReportId(report.getIndustryReportId());
        reportDO.setYear(report.getYear());
        reportDO.setIndustry(report.getIndustry());
        reportDO.setCreateTime(report.getCreateTime());
        reportDO.setPeriod(report.getPeriod());
        reportDO.setPeriodTimeNumber(report.getPeriodTimeNumber());
        reportDO.setIndustryStatisticsId(report.getStat() != null ? report.getStat().getStatId() : null);
        Map<String, String> ids = new HashMap<>(report.getBrandReports().size());
        report.getBrandReports().forEach((s, brandReport) -> ids.put(s, brandReport.getReportId()));
        reportDO.setBrandReportIds(ids);
        return reportDO;
    }

    /**
     * 转换行业报告DO为BO
     *
     * @param reportDO 行业报告DO
     * @return 行业报告BO
     */
    private IndustryReport toIndustryReport(@Nullable IndustryReportDO reportDO) {
        if (reportDO == null) {
            return null;
        }
        logger.info("将行业报告数据对象转换为行业报告业务对象(ID:{})", reportDO.getIndustryReportId());
        IndustryReport report = new IndustryReport();
        report.setIndustryReportId(reportDO.getIndustryReportId());
        report.setIndustry(reportDO.getIndustry());
        report.setCreateTime(reportDO.getCreateTime());
        report.setYear(reportDO.getYear());
        report.setPeriod(reportDO.getPeriod());
        report.setPeriodTimeNumber(reportDO.getPeriodTimeNumber());
        // 获取行业统计数据
        if (reportDO.getIndustryStatisticsId() != null) {
            logger.info("获取行业报告(ID:{})的行业统计数据(ID:{})", reportDO.getIndustryReportId(), reportDO.getIndustryStatisticsId());
            Optional<IndustryStatistics> industryStatisticsServiceById = industryStatisticsService.getById(reportDO.getIndustryStatisticsId());
            report.setStat(industryStatisticsServiceById.orElseThrow(
                    () -> new EntityNotExistException("没有找到ID为" + reportDO.getIndustryStatisticsId() + "的行业统计数据")));
        }
        if (reportDO.getBrandReportIds() != null) {
            Map<String, BrandReport> map = new ConcurrentHashMap<>(reportDO.getBrandReportIds().size());
            reportDO.getBrandReportIds().entrySet().parallelStream().forEach(entry -> {
                logger.info("获取行业报告(ID:{})的品牌报告数据(ID:{})", reportDO.getIndustryReportId(), entry.getValue());
                Optional<BrandReport> brandReport = brandReportService.getById(entry.getValue());
                map.put(entry.getKey(), brandReport.orElseThrow(
                        () -> new EntityNotExistException("没有找到ID为" + entry.getValue() + "的品牌报告")));
            });
            report.setBrandReports(map);
        }
        return report;
    }

    @Override
    @Cacheable(value = "industryReportBuild")
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
        String reportTimeString = LogUtils.getLogTime(year, period, periodTimeNumber);
        logger.info("构建{}行业在{}的行业报告", industry, reportTimeString);

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
            statistics.setStats(Collections.emptyMap());
            report.setStat(statistics);
            return report;
        }

        logger.info("获取行业统计数据");
        IndustryStatistics queryExample = new IndustryStatistics();
        queryExample.setIndustry(industry);
        queryExample.setYear(year);
        queryExample.setPeriod(period);
        queryExample.setPeriodTimeNumber(periodTimeNumber);
        Collection<IndustryStatistics> statSearch = ((Collection<IndustryStatistics>) industryStatisticsService.getAllByExample(queryExample));
        IndustryStatistics statistics;
        if (statSearch.size() == 0) {
            logger.info("没有找到{}行业在{}的统计数据", industry, reportTimeString);
            statistics = industryStatisticsService.countStatistics(industry, year, period, periodTimeNumber);
        } else {
            IndustryStatistics stat = statSearch.iterator().next();
            logger.info("找到{}行业在{}的统计数据，ID:{}", industry, reportTimeString, stat.getStatId());
            statistics = stat;
        }
        report.setStat(statistics);

        final String finalPeriod = period;
        final Integer finalPeriodTimeNumber = periodTimeNumber;
        industryBrands.parallelStream().forEach(brand -> {
            logger.info("获取{}品牌在{}的品牌报告", brand.getBrandName(), reportTimeString);
            BrandReport brandExample = new BrandReport();
            brandExample.setBrandId(brand.getBrandId());
            brandExample.setYear(year);
            brandExample.setPeriod(finalPeriod);
            brandExample.setPeriodTimeNumber(finalPeriodTimeNumber);
            Collection<BrandReport> brandReportSearch = ((Collection<BrandReport>) brandReportService.getAllByExample(brandExample));
            // 目前仅获取其中一份报告，而不管优先级
            if (brandReportSearch.size() > 0) {
                BrandReport brandReport = brandReportSearch.iterator().next();
                logger.info("找到{}品牌在{}的报告，ID:{}", brand.getBrandName(),
                        reportTimeString, brandReport.getReportId());
                report.getBrandReports().put(brand.getBrandId(), brandReport);
            } else {
                logger.info("{}品牌在{}的报告不存在，进入构建", brand.getBrandName(), reportTimeString);
                BrandReport brandReport = brandReportService.buildReport(brand.getBrandId(), year, finalPeriod, finalPeriodTimeNumber);
                report.getBrandReports().put(brand.getBrandId(), brandReport);
            }
        });
        logger.info("构建行业报告完成");
        return report;
    }

    @Override
    public IndustryReport save(IndustryReport report) {
        if (report == null) {
            return null;
        }
        String reportTimeString = LogUtils.getLogTime(report.getYear(), report.getPeriod(), report.getPeriodTimeNumber());
        logger.info("保存{}行业在{}的行业报告(ID:{})", report.getIndustry(), reportTimeString, report.getIndustryReportId());
        // 首先保存各个品牌报告
        report.getBrandReports().entrySet().parallelStream().forEach(entry -> {
            logger.info("保存行业报告(ID:{})中的品牌报告(ID:{})", report.getIndustryReportId(), entry.getValue().getReportId());
            BrandReport saved = brandReportService.save(entry.getValue());
            entry.setValue(saved);
        });
        // 保存行业统计
        logger.info("保存行业报告(ID:{})中的统计数据(ID:{})", report.getIndustryReportId(), report.getStat().getStatId());
        IndustryStatistics savedStat = industryStatisticsService.save(report.getStat());
        report.setStat(savedStat);
        // 执行转换
        logger.info("转换行业报告为数据库格式");
        IndustryReportDO reportDO = toDO(report);
        logger.info("正在保存行业报告(ID:{})到数据库", report.getIndustryReportId());
        IndustryReportDO save = doRepository.save(reportDO);
        // 设置必要的ID
        report.setIndustryReportId(save.getIndustryReportId());
        logger.info("保存行业报告(ID:{})完成", report.getIndustryReportId());
        return report;
    }

    @Override
    public IndustryReport partialUpdate(String s, IndustryReport updateVal) {
        throw new MethodNotSupportException("行业报告不支持部分更新");
    }

    @Override
    public Iterable<IndustryReport> saveAll(Iterable<IndustryReport> reports) {
        return ((Collection<IndustryReport>) reports).parallelStream().map(this::save).collect(Collectors.toSet());
    }

    /**
     * 本删除方法仅仅删除行业报告，不删除其附带的行业统计信息和品牌报告
     *
     * @param report 要删除的实体
     */
    @Override
    public void delete(IndustryReport report) {
        doRepository.deleteById(report.getIndustryReportId());
    }

    @Override
    public void deleteById(String id) {
        doRepository.deleteById(id);
    }

    @Override
    public void deleteAll(Iterable<IndustryReport> reports) {
        Set<IndustryReportDO> collect = ((Collection<IndustryReport>) reports).parallelStream().map(this::toDO).collect(Collectors.toSet());
        doRepository.deleteAll(collect);
    }

    @Override
    public Optional<IndustryReport> getById(String s) {
        Optional<IndustryReportDO> byId = doRepository.findById(s);
        //noinspection OptionalIsPresent,ConstantConditions
        return byId.isPresent() ? Optional.of(toIndustryReport(byId.get())) : Optional.empty();
    }

    @Override
    public Iterable<IndustryReport> getAllById(Iterable<String> strings) {
        Iterable<IndustryReportDO> allById = doRepository.findAllById(strings);
        return ((Collection<IndustryReportDO>) allById).parallelStream().map(this::toIndustryReport).collect(Collectors.toSet());
    }

    @Override
    public Iterable<IndustryReport> getAllByExample(IndustryReport example) {
        if (example == null) {
            return Collections.emptyList();
        }
        logger.info("使用示例对象{}进行查询", example.toString());
        List<IndustryReportDO> all = doRepository.findAll(Example.of(toDO(example)));
        return all.parallelStream().map(this::toIndustryReport).collect(Collectors.toList());
    }

    @Override
    public boolean existById(String s) {
        return doRepository.existsById(s);
    }

    @Override
    public Iterable<IndustryReport> getAll() {
        List<IndustryReportDO> all = doRepository.findAll();
        return all.parallelStream().map(this::toIndustryReport).collect(Collectors.toList());
    }

    @Override
    public boolean isIdOfEntity(String s, IndustryReport entity) {
        return Objects.equals(s, entity.getIndustryReportId());
    }
}
