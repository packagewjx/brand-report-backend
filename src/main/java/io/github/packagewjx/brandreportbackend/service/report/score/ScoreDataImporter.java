package io.github.packagewjx.brandreportbackend.service.report.score;


import io.github.packagewjx.brandreportbackend.BrandService;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.exception.EntityNotExistException;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.service.report.BrandReportDataImporter;
import io.github.packagewjx.brandreportbackend.service.report.score.scorecounter.Context;
import io.github.packagewjx.brandreportbackend.service.report.score.scorecounter.IndexScoreCounter;
import io.github.packagewjx.brandreportbackend.service.report.score.scorecounter.ScoreCounterFactory;
import io.github.packagewjx.brandreportbackend.service.statistics.StatisticsCounter;
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
    private final IndustryStatisticsService industryStatisticsService;
    private final BrandService brandService;
    private final StatisticsCounter statisticsCounter;
    /**
     * 指标ID与对应的计算分数类的Map
     */
    private Map<String, IndexScoreCounter> counters;
    /**
     * 键为保存的指标，值为所有算分的指标，使用这些指标算完分数后，保存到键的指标中
     */
    private Map<Index, Set<Index>> countMap;

    public ScoreDataImporter(IndustryStatisticsService industryStatisticsService, BrandService brandService, StatisticsCounter statisticsCounter, IndexService indexService) {
        this.industryStatisticsService = industryStatisticsService;
        this.brandService = brandService;
        this.statisticsCounter = statisticsCounter;

        // 获取所有指标
        Collection<Index> indices = (Collection<Index>) indexService.getAll();

        // 获取保存分数的指标
        Set<Index> scoreIndices = indices.parallelStream().filter(index -> index.getAnnotations() != null)
                .filter(index -> index.getAnnotations().containsKey(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR))
                .collect(Collectors.toSet());
        countMap = new ConcurrentHashMap<>();
        scoreIndices.parallelStream().forEach(index -> {
            Set<Index> childIndices = getChildIndices(index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_SCORE_INDEX_FOR), indices);
            countMap.put(index, childIndices);
        });

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
        Collection<IndustryStatistics> byIndustry = industryStatisticsService.getByIndustry(industry);
        IndustryStatistics ret;
        if (Constants.PERIOD_ANNUAL.equals(period)) {
            ret = byIndustry.stream()
                    .filter(industryStatistics -> year.equals(industryStatistics.getYear()))
                    .findAny().orElse(null);
        } else {
            if (periodTimeNumber == null || period == null) {
                throw new IllegalArgumentException("periodTimeNumber与period不能为空");
            }
            ret = byIndustry.stream()
                    .filter(industryStatistics -> period.equals(industryStatistics.getPeriod()))
                    .filter(industryStatistics -> periodTimeNumber.equals(industryStatistics.getMonth()) && year.equals(industryStatistics.getYear()))
                    .findAny().orElse(null);

        }

        // 若没有保存，则返回计算值
        return ret != null ? ret : statisticsCounter.count(industry, year, period, periodTimeNumber);
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
                brandReport.getYear(), brandReport.getPeriodTimeNumber());
        Context ctx = new Context();
        ctx.industryStatistics = industryStatistics;

        // 计算分数。每个map的entry使用一条线程进行计算
        countMap.entrySet().parallelStream().forEach(entry -> {
            Set<Index> indices = entry.getValue();
            Double sum = indices.stream()
                    .map(index -> {
                        // 这里不应该再产生NPE，因为对所有的指标均进行了加载
                        return counters.get(index.getIndexId()).countScore(brandReport, ctx);
                    })
                    .reduce(Double::sum).orElse(0.0);
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
