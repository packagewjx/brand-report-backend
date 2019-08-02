package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.BaseStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.NumberStatistics;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;
import io.github.packagewjx.brandreportbackend.service.report.score.StepScoreDefinition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-19
 **/
public class StepScoreCounter implements IndexScoreCounter {
    /**
     * 特殊的参数，代表分区分界点指定位置使用行业平均值代替
     */
    public static final String ARG_INDUSTRY_AVERAGE = "average";

    private static final String DEFAULT_LOWER_BOUND_EXCLUSIVE = "false";

    private StepScoreDefinition definition;
    private List<Object> interval;
    private String indexId;
    private boolean lowerBoundExclusive;

    /**
     * 用于性能优化
     */
    private boolean hasDynamicVariable = false;

    public StepScoreCounter(Index index) {
        if (index == null) {
            throw new IllegalArgumentException("Index不能为空");
        }
        if (index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE) == null ||
                !ScoreAnnotations.StepScoreCounter.ANNOTATION_VALUE_TYPE.equals(index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE))
                || !index.getAnnotations().containsKey(ScoreAnnotations.StepScoreCounter.ANNOTATION_KEY_SCORE_DEFINITION)) {
            throw new IllegalArgumentException("Index'" + index.getIndexId() + "'注解有误");
        }
        this.indexId = index.getIndexId();
        // 设置
        String lower = index.getAnnotations().getOrDefault(ScoreAnnotations.StepScoreCounter.ANNOTATION_KEY_LOWER_BOUND_EXCLUSIVE, DEFAULT_LOWER_BOUND_EXCLUSIVE);
        this.lowerBoundExclusive = Constants.ANNOTATION_VALUE_TRUE.equals(lower);

        // 解析Definition
        ObjectMapper mapper = new ObjectMapper();
        try {
            definition = mapper.readValue(index.getAnnotations().get(ScoreAnnotations.StepScoreCounter.ANNOTATION_KEY_SCORE_DEFINITION), StepScoreDefinition.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(indexId + "的StepScoreDefinition读取错误，检查是否为有效的StepScoreDefinition的json", e);
        }

        // 检查definition
        if (definition.getIntervalSplit() == null || definition.getIntervalSplit().size() == 0
                || definition.getIntervalScore() == null || definition.getIntervalScore().size() == 0) {
            throw new IllegalArgumentException("intervalSplit和intervalScore不能为空");
        }
        if (definition.getIntervalScore().size() != definition.getIntervalSplit().size() + 1) {
            throw new IllegalArgumentException(indexId + "的区间分区值列表的长度不是分数值列表长度加1");
        }

        interval = new ArrayList<>(definition.getIntervalSplit().size());
        definition.getIntervalSplit().forEach(o -> {
            if (o instanceof String) {
                this.hasDynamicVariable = true;
                if (ARG_INDUSTRY_AVERAGE.equals(o)) {
                    interval.add((Function<Context, Double>) ctx -> {
                        if (ctx == null) {
                            throw new IllegalArgumentException("Context不能为空");
                        }
                        IndustryStatistics industryStatistics = ctx.industryStatistics;
                        if (industryStatistics == null || industryStatistics.getStats() == null
                                || !industryStatistics.getStats().containsKey(this.indexId)) {
                            throw new IllegalArgumentException("行业统计数据不包含" + this.indexId);
                        }
                        BaseStatistics baseStatistics = industryStatistics.getStats().get(this.indexId);
                        if (!(baseStatistics instanceof NumberStatistics)) {
                            throw new IllegalArgumentException(this.indexId + "行业统计数据不是数字类型，错误");
                        }

                        return ((NumberStatistics) baseStatistics).getAverage();
                    });
                } else {
                    throw new IllegalArgumentException("不支持的变量" + o);
                }
            } else if (o instanceof Number) {
                // 保证加进去的是Double
                interval.add(((Number) o).doubleValue());
            } else {
                throw new IllegalArgumentException("StepScoreDefinition的ScoreSplit有误");
            }
        });

        if (!hasDynamicVariable) {
            // 预先排好序
            this.interval.sort(Comparator.comparingDouble(o -> ((Double) o)));
        }
    }

    @Override
    public double countScore(BrandReport brandReport, Context ctx) {
        if (brandReport == null || brandReport.getData() == null) {
            throw new IllegalArgumentException("brandReport有误");
        }
        Object d = brandReport.getData().get(indexId);
        if (d == null) {
            return 0;
        }
        if (!(d instanceof Number)) {
            throw new IllegalArgumentException("brandReport的" + indexId + "不是数字");
        }
        double val = ((Number) d).doubleValue();

        List<Double> split = new ArrayList<>(this.interval.size());
        if (hasDynamicVariable) {
            for (Object o : interval) {
                if (o instanceof Double) {
                    split.add((Double) o);
                } else if (o instanceof Function) {
                    // 暂时不检查泛型
                    //noinspection unchecked
                    split.add((Double) ((Function) o).apply(ctx));
                }
            }
            split.sort(Double::compareTo);
        } else {
            // 使用特殊的转换，但是很危险
            //noinspection unchecked
            split = (List<Double>) (Object) interval;
        }
        // 到这里，split应该是排好序的数组
        int intervalNum = 0;
        if (this.lowerBoundExclusive) {
            for (int i = 0; i < split.size(); i++) {
                if (Double.compare(val, split.get(i)) <= 0) {
                    break;
                }
                intervalNum++;
            }
        } else {
            for (int i = 0; i < split.size(); i++) {
                if (Double.compare(val, split.get(i)) < 0) {
                    break;
                }
                intervalNum++;
            }
        }
        return definition.getIntervalScore().get(intervalNum);
    }
}
