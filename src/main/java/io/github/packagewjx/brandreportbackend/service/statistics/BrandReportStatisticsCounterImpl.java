package io.github.packagewjx.brandreportbackend.service.statistics;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.Annotations;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.BaseStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.ClassStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.NumberStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-9-16
 **/
@Service
public class BrandReportStatisticsCounterImpl implements BrandReportStatisticsCounter {
    private static final Logger logger = LoggerFactory.getLogger(BrandReportStatisticsCounterImpl.class);

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
    public IndustryStatistics count(Collection<BrandReport> reports, Collection<Index> indices) {
        // 首先检查报告是否有同样的年份，统计时长，和月份
        if (reports == null || reports.size() == 0) {
            logger.warn("空报告集，无法统计");
            return null;
        }
        BrandReport firstReport = reports.iterator().next();
        int year = firstReport.getYear();
        String period = firstReport.getPeriod();
        Integer periodTimeNumber = firstReport.getPeriodTimeNumber();

        reports.forEach(brandReport -> {
            if (year != brandReport.getYear() || !period.equals(brandReport.getPeriod())
                    || !Objects.equals(periodTimeNumber, brandReport.getPeriodTimeNumber())) {
                throw new IllegalArgumentException("报告的时间不一致");
            }
        });

        // 创建结果
        IndustryStatistics stat = new IndustryStatistics();
        stat.setYear(year);
        stat.setPeriod(period);
        stat.setPeriodTimeNumber(periodTimeNumber);
        Map<String, BaseStatistics> statisticsMap = new ConcurrentHashMap<>(indices.size());

        // 布尔类型统计
        Set<Index> boolIndices = indices.parallelStream().filter(index -> Index.TYPE_BOOL.equals(index.getType())).collect(Collectors.toSet());
        boolIndices.parallelStream().forEach(index -> {
            ClassStatistics classStatistics = new ClassStatistics();
            long trueCount = reports.stream().map(c -> toBool(c.getData().getOrDefault(index.getIndexId(), false)))
                    .filter(o -> o).count();
            long falseCount = reports.stream().map(c -> toBool(c.getData().getOrDefault(index.getIndexId(), false)))
                    .filter(o -> !o).count();
            Map<Boolean, Integer> result = new HashMap<>(2);
            result.put(true, (int) trueCount);
            result.put(false, (int) falseCount);
            classStatistics.setCounts(result);
            classStatistics.setTotal((int) (trueCount + falseCount));
            statisticsMap.put(index.getIndexId(), classStatistics);
        });

        // 枚举类型统计
        Set<Index> enumIndices = indices.parallelStream().filter(index -> Index.TYPE_ENUM.equals(index.getType())).collect(Collectors.toSet());
        enumIndices.parallelStream().forEach(index -> {
            ClassStatistics statistics = new ClassStatistics();
            Map<Object, Integer> counts = new HashMap<>(10);
            reports.forEach(report -> {
                Object o = report.getData().get(index.getIndexId());
                // 防止为空时，无法转换为json
                if (o == null) {
                    o = "null";
                }
                Integer count = counts.getOrDefault(o, 0);
                counts.put(o, count + 1);
            });
            int total = 0;
            Iterator<Object> iterator = counts.keySet().iterator();
            while (iterator.hasNext()) {
                total += counts.get(iterator.next());
            }
            statistics.setCounts(counts);
            statistics.setTotal(total);
            statisticsMap.put(index.getIndexId(), statistics);
        });

        // 数字类型统计，仅仅统计不是null的
        Set<Index> numberIndices = indices.parallelStream().filter(index -> Index.TYPE_NUMBER.equals(index.getType())).collect(Collectors.toSet());
        numberIndices.parallelStream().forEach(index -> {
            NumberStatistics numberStatistics = new NumberStatistics();
            double sum = reports.stream()
                    .mapToDouble(value -> ((Number) value.getData().getOrDefault(index.getIndexId(), 0)).doubleValue())
                    .sum();
            double count;
            if (Constants.ANNOTATION_VALUE_TRUE.equals(index.getAnnotations().get(Annotations.STAT_ANNOTATION_NULL_AS_ZERO))) {
                count = reports.size();
            } else {
                count = reports.stream().filter(collection -> collection.getData().containsKey(index.getIndexId())).count();
            }
            numberStatistics.setSum(sum);
            numberStatistics.setAverage(sum / count);
            statisticsMap.put(index.getIndexId(), numberStatistics);
        });

        stat.setTotal(reports.size());
        stat.setStats(statisticsMap);
        return stat;
    }
}
