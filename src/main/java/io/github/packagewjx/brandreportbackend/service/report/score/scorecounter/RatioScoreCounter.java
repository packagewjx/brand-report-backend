package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.BaseStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.NumberStatistics;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-19
 **/
public class RatioScoreCounter implements IndexScoreCounter {
    private String indexId;
    private double totalScore;

    public RatioScoreCounter(Index index) {
        if (index == null || index.getAnnotations() == null) {
            throw new IllegalArgumentException("Index及注解不能为null");
        }
        indexId = index.getIndexId();
        if (!ScoreAnnotations.RatioScoreCounter.ANNOTATION_VALUE_TYPE.equals(index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE))) {
            throw new IllegalArgumentException(indexId + "计分类型不是本类处理的计分类型");
        }

        String scoreString = index.getAnnotations().get(ScoreAnnotations.RatioScoreCounter.ANNOTATION_KEY_TOTAL_SCORE);
        if (scoreString == null) {
            throw new IllegalArgumentException(indexId + "没有指定比例总分");
        }
        try {
            totalScore = Double.parseDouble(scoreString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(indexId + "比例总分不是数字", e);
        }
    }

    @Override
    public double countScore(BrandReport brandReport, Context ctx) {
        if (brandReport == null || brandReport.getData() == null) {
            throw new IllegalArgumentException("brandReport为null");
        }
        Object data = brandReport.getData().get(indexId);
        if (data == null) {
            return 0;
        }
        if (!(data instanceof Number)) {
            throw new IllegalArgumentException("brandReport的" + indexId + "不是数字");
        }
        double val = ((Number) data).doubleValue();

        if (ctx.industryStatistics == null || ctx.industryStatistics.getStats() == null || !ctx.industryStatistics.getStats().containsKey(indexId)) {
            throw new IllegalArgumentException(indexId + "行业统计数据不能为空");
        }
        BaseStatistics baseStatistics = ctx.industryStatistics.getStats().get(indexId);
        if (!(baseStatistics instanceof NumberStatistics)) {
            throw new IllegalArgumentException(indexId + "行业统计数据类型不是数字");
        }

        double sum = ((NumberStatistics) baseStatistics).getSum();
        return val / sum * totalScore;
    }
}
