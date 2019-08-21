package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.BaseStatistics;
import io.github.packagewjx.brandreportbackend.domain.statistics.data.NumberStatistics;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-19
 **/
public class RatioScoreCounter implements IndexScoreCounter {
    private static final Logger logger = LoggerFactory.getLogger(RatioScoreCounter.class);
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

        logger.info("指标{}使用比例计分器，总分为{}", indexId, totalScore);
    }

    @Override
    public double countScore(BrandReport brandReport, Context ctx) {
        if (brandReport == null || brandReport.getData() == null) {
            throw new IllegalArgumentException("brandReport为null");
        }
        Object data = brandReport.getData().get(indexId);
        if (data == null) {
            logger.trace("品牌报告(ID:{})的{}指标的值为null，分数为0", brandReport.getReportId(), indexId);
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
        double score = val / sum * totalScore;
        logger.trace("品牌报告(ID:{})的{}指标的值为{}，行业数据为{}，分数{}", brandReport.getReportId(), indexId, val, sum, score);
        return score;
    }
}
