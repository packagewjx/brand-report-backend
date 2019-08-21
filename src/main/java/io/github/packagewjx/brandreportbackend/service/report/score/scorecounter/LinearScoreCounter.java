package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-19
 **/
public class LinearScoreCounter implements IndexScoreCounter {
    public static final double DEFAULT_LOWER_BOUND = -Double.MAX_VALUE;
    public static final double DEFAULT_UPPER_BOUND = Double.MAX_VALUE;
    private static final Logger logger = LoggerFactory.getLogger(LinearScoreCounter.class);

    private String indexId;
    private double slope;
    private double intercept;
    private double lowerBound;
    private double upperBound;

    public LinearScoreCounter(String indexId, double slope, double intercept) {
        this.indexId = indexId;
        this.slope = slope;
        this.intercept = intercept;
        lowerBound = DEFAULT_LOWER_BOUND;
        upperBound = DEFAULT_UPPER_BOUND;
    }

    public LinearScoreCounter(String indexId, double slope, double intercept, double lowerBound, double upperBound) {
        this.indexId = indexId;
        this.slope = slope;
        this.intercept = intercept;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public LinearScoreCounter(Index index) {
        if (index == null || index.getAnnotations() == null) {
            throw new IllegalArgumentException("Index及注解不能为空");
        }
        this.indexId = index.getIndexId();
        if (!ScoreAnnotations.LinearScoreCounter.ANNOTATION_VALUE_TYPE.equals(index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE))) {
            throw new IllegalArgumentException(indexId + "不适用本类进行计算");
        }

        String slopeString = index.getAnnotations().get(ScoreAnnotations.LinearScoreCounter.ANNOTATION_KEY_SLOPE);
        String interceptString = index.getAnnotations().get(ScoreAnnotations.LinearScoreCounter.ANNOTATION_KEY_INTERCEPT);
        if (slopeString == null || interceptString == null) {
            throw new IllegalArgumentException(indexId + "的截距或斜率为空");
        }

        try {
            slope = Double.parseDouble(slopeString);
            intercept = Double.parseDouble(interceptString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(indexId + "的斜率或截距不是数字");
        }

        // 获取上下界
        String lowerBoundString = index.getAnnotations().get(ScoreAnnotations.LinearScoreCounter.ANNOTATION_KEY_X_LOWER_BOUND);
        String upperBoundString = index.getAnnotations().get(ScoreAnnotations.LinearScoreCounter.ANNOTATION_KEY_X_UPPER_BOUND);
        try {
            lowerBound = lowerBoundString == null ? DEFAULT_LOWER_BOUND : Double.parseDouble(lowerBoundString);
        } catch (NumberFormatException e) {
            logger.warn("{}的下界{}不是数字，使用默认值{}", indexId, lowerBoundString, DEFAULT_LOWER_BOUND);
            lowerBound = DEFAULT_LOWER_BOUND;
        }
        try {
            upperBound = upperBoundString == null ? DEFAULT_UPPER_BOUND : Double.parseDouble(upperBoundString);
        } catch (NumberFormatException e) {
            logger.warn("{}的上界{}不是数字，使用默认值{}", indexId, upperBoundString, DEFAULT_UPPER_BOUND);
            upperBound = DEFAULT_UPPER_BOUND;
        }

        logger.info("指标{}使用线性计分器，斜率为{}，截距为{}，上界为{}，下界为{}", indexId, slope, intercept, lowerBound, upperBound);
    }


    @Override
    public double countScore(BrandReport brandReport, Context ctx) {
        if (brandReport == null || brandReport.getData() == null) {
            throw new IllegalArgumentException("数据为空");
        }

        Object o = brandReport.getData().get(indexId);
        if (o == null) {
            logger.trace("品牌报告(ID:{})的{}指标的值为null，分数为0", brandReport.getReportId(), indexId);
            return 0;
        }
        if (!(o instanceof Number)) {
            throw new IllegalArgumentException(indexId + "数据不是数字");
        }

        double val = ((Number) o).doubleValue();
        if (Double.compare(val, upperBound) <= 0 && Double.compare(val, lowerBound) >= 0) {
            double score = slope * val + intercept;
            logger.trace("品牌报告(ID:{})的{}指标的值{}，分数为{}", brandReport.getReportId(), indexId, val, score);
            return score;
        } else {
            // 不在范围内
            logger.warn("品牌报告(ID:{})的{}指标的值{}超出范围[{}, {}]", brandReport.getReportId(), indexId, val,
                    lowerBound, upperBound);
            return 0;
        }

    }
}
