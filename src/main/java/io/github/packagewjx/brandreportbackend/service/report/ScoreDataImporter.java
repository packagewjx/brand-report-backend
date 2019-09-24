package io.github.packagewjx.brandreportbackend.service.report;


import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.exception.EntityNotExistException;
import io.github.packagewjx.brandreportbackend.service.BrandService;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;
import io.github.packagewjx.brandreportbackend.service.report.score.scorecounter.Context;
import io.github.packagewjx.brandreportbackend.service.report.score.scorecounter.IndexScoreCounter;
import io.github.packagewjx.brandreportbackend.service.report.score.scorecounter.ScoreCounterFactory;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 负责计算分数的DataImporter
 *
 * @author Junxian Wu
 */
@Service
public class ScoreDataImporter implements BrandReportDataImporter {
    private final static Logger logger = LoggerFactory.getLogger(ScoreDataImporter.class);

    private final IndustryStatisticsService industryStatisticsService;
    private final BrandService brandService;
    /**
     * 指标ID与对应的计算分数类的Map
     */
    private Map<String, IndexScoreCounter> counters;
    /**
     * 键为保存的指标，值为所有算分的指标，使用这些指标算完分数后，保存到键的指标中
     */
    private Map<Index, Set<Index>> countMap;

    public ScoreDataImporter(@Lazy IndustryStatisticsService industryStatisticsService, BrandService brandService, IndexService indexService) {
        logger.debug("构建ScoreDataImporter中");
        this.industryStatisticsService = industryStatisticsService;
        this.brandService = brandService;

        logger.debug("获取所有指标");
        // 获取所有指标
        Collection<Index> indices = (Collection<Index>) indexService.getAll();

        logger.debug("提取出用于保存分数的指标");
        // 获取保存分数的指标
        Set<Index> scoreIndices = indices.parallelStream().filter(index -> index.getAnnotations() != null)
                .filter(index -> index.getAnnotations().containsKey(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR))
                .collect(Collectors.toSet());
        logger.debug("共{}个分数指标", scoreIndices.size());
        scoreIndices.forEach(index -> logger.trace("计分指标{}，用于保存{}的叶子指标的分数",
                index.getIndexId(), index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR)));

        logger.debug("根据每个计分指标所汇总的子指标表");
        countMap = new ConcurrentHashMap<>();
        scoreIndices.parallelStream().forEach(index -> {
            Set<Index> childIndices = getChildIndices(index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR), indices);
            countMap.put(index, childIndices);
        });

        logger.debug("构建分数计算类中");
        // 初始化计算类
        counters = new HashMap<>(indices.size());
        indices.forEach(index -> counters.put(index.getIndexId(), ScoreCounterFactory.instance.build(index)));
    }

    /**
     * 查询或计算行业统计数据
     *
     * @param industry         行业
     * @param period           统计时长
     * @param year             年份
     * @param periodTimeNumber 年内统计时间序号
     * @return 行业统计数据，保证非空
     */
    private IndustryStatistics getIndustryStatisticsAndTime(String industry, String period, Integer year, Integer periodTimeNumber) {
        if (period == null) {
            logger.error("period不能为空");
            throw new IllegalArgumentException("period不能为空");
        }
        logger.info("获取行业{}在{}的统计数据", industry, LogUtils.getLogTime(year, period, periodTimeNumber));
        IndustryStatistics example = new IndustryStatistics();
        example.setIndustry(industry);
        Collection<IndustryStatistics> byIndustry = ((Collection<IndustryStatistics>) industryStatisticsService.getAllByExample(example));
        IndustryStatistics ret;
        if (Constants.PERIOD_ANNUAL.equals(period)) {
            ret = byIndustry.stream()
                    .filter(industryStatistics -> year.equals(industryStatistics.getYear()))
                    .findAny().orElse(null);
        } else {
            if (periodTimeNumber == null) {
                throw new IllegalArgumentException("periodTimeNumber不能为空");
            }
            ret = byIndustry.stream()
                    .filter(industryStatistics -> period.equals(industryStatistics.getPeriod()))
                    .filter(industryStatistics -> periodTimeNumber.equals(industryStatistics.getPeriodTimeNumber()) && year.equals(industryStatistics.getYear()))
                    .findAny().orElse(null);
        }

        if (ret == null) {
            logger.info("目前没有保存{}在{}的统计数据，开始计算", industry, LogUtils.getLogTime(year, period, periodTimeNumber));
            return industryStatisticsService.countStatistics(industry, year, period, periodTimeNumber);
        } else {
            return ret;
        }
    }

    @Override
    public BrandReport importData(BrandReport brandReport) {
        logger.info("计算品牌ID{}在{}品牌报告的分数", brandReport.getBrandId(),
                LogUtils.getLogTime(brandReport.getYear(), brandReport.getPeriod(), brandReport.getPeriodTimeNumber()));
        Optional<Brand> oBrand = brandService.getById(brandReport.getBrandId());
        if (!oBrand.isPresent()) {
            logger.error("不存在品牌Id{}的品牌", brandReport.getBrandId());
            throw new EntityNotExistException("不存在BrandId为" + brandReport.getBrandId() + "的品牌");
        }
        if (brandReport.getYear() == null) {
            logger.error("年份不能为空");
            throw new IllegalArgumentException("year不能为空");
        }

        String industry = oBrand.get().getIndustry();
        logger.info("获取行业{}在{}的统计结果", industry,
                LogUtils.getLogTime(brandReport.getYear(), brandReport.getPeriod(), brandReport.getPeriodTimeNumber()));
        IndustryStatistics industryStatistics = getIndustryStatisticsAndTime(industry, brandReport.getPeriod(),
                brandReport.getYear(), brandReport.getPeriodTimeNumber());
        Context ctx = new Context();
        ctx.industryStatistics = industryStatistics;

        // 计算分数。每个map的entry使用一条线程进行计算
        countMap.entrySet().parallelStream().forEach(entry -> {
            Set<Index> indices = entry.getValue();
            Double sum = indices.stream()
                    .map(index -> {
                        // 这里不应该再产生NPE，因为对所有的指标均进行了加载
                        double score = counters.get(index.getIndexId()).countScore(brandReport, ctx);
                        logger.trace("品牌ID{}在{}的{}分数为{}", brandReport.getBrandId(),
                                LogUtils.getLogTime(brandReport.getYear(), brandReport.getPeriod(), brandReport.getPeriodTimeNumber()),
                                index.getIndexId(), score);
                        return score;
                    })
                    .reduce(Double::sum).orElse(0.0);
            logger.debug("品牌ID{}在{}的{}分数为{}", brandReport.getBrandId(),
                    LogUtils.getLogTime(brandReport.getYear(), brandReport.getPeriod(), brandReport.getPeriodTimeNumber()),
                    entry.getKey().getIndexId(), sum);
            brandReport.getData().put(entry.getKey().getIndexId(), sum);
        });

        return brandReport;
    }


    /**
     * 获取rootIndexId对应Index的子Index对象集合
     *
     * @param rootIndexId 根IndexId
     * @param indices     所有Index
     * @return 子Index集合
     */
    private Set<Index> getChildIndices(String rootIndexId, Collection<Index> indices) {
        if (rootIndexId == null || "".equals(rootIndexId)) {
            return Collections.emptySet();
        }
        Set<Index> result = new HashSet<>();
        List<String> queue = new ArrayList<>();
        queue.add(rootIndexId);
        while (queue.size() > 0) {
            String parentId = queue.get(0);
            queue.remove(0);
            // 叶子节点加入结果
            indices.parallelStream()
                    .filter(index -> parentId.equals(index.getParentIndexId()))
                    .filter(index -> !index.getType().equals(Index.TYPE_INDICES))
                    .forEach(result::add);
            // 中间节点加入队列
            indices.parallelStream()
                    .filter(index -> parentId.equals(index.getParentIndexId()))
                    .filter(index -> index.getType().equals(Index.TYPE_INDICES))
                    .forEach(index -> queue.add(index.getIndexId()));
        }
        return result;
    }
}
