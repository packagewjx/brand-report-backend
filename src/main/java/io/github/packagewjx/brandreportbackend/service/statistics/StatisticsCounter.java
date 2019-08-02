package io.github.packagewjx.brandreportbackend.service.statistics;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.Annotations;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.BaseStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.ClassStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.NumberStatistics;
import io.github.packagewjx.brandreportbackend.repository.BrandRepository;
import io.github.packagewjx.brandreportbackend.service.CollectionService;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
@Service
public class StatisticsCounter implements Statistics {
    private final CollectionService collectionService;

    private final BrandRepository brandRepository;

    private final IndexService indexService;

    public StatisticsCounter(CollectionService collectionService, BrandRepository brandRepository, IndexService indexService) {
        this.brandRepository = brandRepository;
        this.collectionService = collectionService;
        this.indexService = indexService;
    }

    private boolean toBool(Object val) {
        if (val instanceof Number) {
            return ((Number) val).intValue() != 0;
        } else if (val instanceof Boolean) {
            return (Boolean) val;
        } else if (val instanceof String) {
            return Constants.ANNOTATION_VALUE_TRUE.equals(val);
        } else {
            // 其他类型就看是否是null
            return val != null;
        }
    }

    @Override
    public IndustryStatistics count(String industry, Integer year, Integer month, Integer quarter, String period) {
        IndustryStatistics stat = new IndustryStatistics();
        stat.setYear(year);
        stat.setMonth(month);
        stat.setQuarter(quarter);
        stat.setPeriod(period);
        stat.setIndustry(industry);

        List<Brand> industryBrand = brandRepository.findByIndustry(industry);
        List<Collection> collections = industryBrand.parallelStream()
                .map(brand -> collectionService.getCombinedOneByTimeAndBrand(brand.getBrandId(), period, year, month, quarter))
                .collect(Collectors.toList());

        List<Index> childIndices = indexService.getAllLeafIndices();
        Map<String, BaseStatistics> statisticsMap = new ConcurrentHashMap<>(childIndices.size());
        // 布尔类型统计
        Set<Index> boolIndices = childIndices.parallelStream().filter(index -> Index.TYPE_BOOL.equals(index.getType())).collect(Collectors.toSet());
        boolIndices.parallelStream().forEach(index -> {
            ClassStatistics classStatistics = new ClassStatistics();
            long trueCount = collections.stream().map(c -> toBool(c.getData().getOrDefault(index.getIndexId(), false)))
                    .filter(o -> o).count();
            long falseCount = collections.stream().map(c -> toBool(c.getData().getOrDefault(index.getIndexId(), false)))
                    .filter(o -> !o).count();
            Map<Boolean, Integer> result = new HashMap<>(2);
            result.put(true, (int) trueCount);
            result.put(false, (int) falseCount);
            classStatistics.setCounts(result);
            statisticsMap.put(index.getIndexId(), classStatistics);
        });

        // 枚举类型统计
        Set<Index> enumIndices = childIndices.parallelStream().filter(index -> Index.TYPE_ENUM.equals(index.getType())).collect(Collectors.toSet());
        enumIndices.parallelStream().forEach(index -> {
            ClassStatistics statistics = new ClassStatistics();
            Map<Object, Integer> counts = new HashMap<>(10);
            collections.forEach(collection -> {
                Object o = collection.getData().get(index.getIndexId());
                Integer count = counts.getOrDefault(o, 0);
                counts.put(o, count + 1);
            });
            statistics.setCounts(counts);
            statisticsMap.put(index.getIndexId(), statistics);
        });

        // 数字类型统计，仅仅统计不是null的
        Set<Index> numberIndices = childIndices.parallelStream().filter(index -> Index.TYPE_NUMBER.equals(index.getType())).collect(Collectors.toSet());
        numberIndices.parallelStream().forEach(index -> {
            NumberStatistics numberStatistics = new NumberStatistics();
            double sum = collections.stream()
                    .mapToDouble(value -> ((Number) value.getData().getOrDefault(index.getIndexId(), 0)).doubleValue())
                    .sum();
            double count;
            if (Constants.ANNOTATION_VALUE_TRUE.equals(index.getAnnotations().get(Annotations.STAT_ANNOTATION_NULL_AS_ZERO))) {
                count = collections.size();
            } else {
                count = collections.stream().filter(collection -> collection.getData().containsKey(index.getIndexId())).count();
            }
            numberStatistics.setSum(sum);
            numberStatistics.setAverage(sum / count);
            statisticsMap.put(index.getIndexId(), numberStatistics);
        });

        stat.setTotal(industryBrand.size());
        stat.setStats(statisticsMap);
        return stat;
    }
}
