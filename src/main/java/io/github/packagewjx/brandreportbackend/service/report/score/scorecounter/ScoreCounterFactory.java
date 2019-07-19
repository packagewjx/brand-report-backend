package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;

/**
 * 负责构造符合IndexScoreCounter接口的实现类
 *
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-19
 **/
public class ScoreCounterFactory {
    public static final ScoreCounterFactory instance = new ScoreCounterFactory();

    private ScoreCounterFactory() {

    }

    /**
     * 构造IndexScoreCounter
     *
     * @param index Counter计算的指标
     * @return 若没有计分类型，则返回一个假Counter，仅仅返回0分。
     */
    public IndexScoreCounter build(Index index) {
        if (index == null || index.getAnnotations() == null) {
            throw new IllegalArgumentException("Index为空或无注解");
        }

        String type = index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE);
        if (type == null) {
            return DummyCounter.instance;
        }

        if (ScoreAnnotations.BoolScoreCounter.ANNOTATION_VALUE_TYPE.equals(type)) {
            // 布尔类型
            return new BoolScoreCounter(index);
        } else if (ScoreAnnotations.StepScoreCounter.ANNOTATION_VALUE_TYPE.equals(type)) {
            // 阶梯型
            return new StepScoreCounter(index);
        } else if (ScoreAnnotations.RatioScoreCounter.ANNOTATION_VALUE_TYPE.equals(type)) {
            return new RatioScoreCounter(index);
        } else if (ScoreAnnotations.EnumScoreCounter.ANNOTATION_VALUE_TYPE.equals(type)) {
            return new EnumScoreCounter(index);
        } else if (ScoreAnnotations.LinearScoreCounter.ANNOTATION_VALUE_TYPE.equals(type)) {
            return new LinearScoreCounter(index);
        } else if (ScoreAnnotations.MultiplyScoreCounter.ANNOTATION_VALUE_TYPE.equals(type)) {
            // 直接使用线性函数代替
            return multiply(index);
        } else if (ScoreAnnotations.ScoreRatioScoreCounter.ANNOTATION_VALUE_TYPE.equals(type)) {
            return scoreRatio(index);
        } else {
            return DummyCounter.instance;
        }
    }

    private LinearScoreCounter scoreRatio(Index index) {
        String totalScoreString = index.getAnnotations().get(ScoreAnnotations.ScoreRatioScoreCounter.ANNOTATION_KEY_SCORE_RATIO_TOTAL_SCORE);
        if (totalScoreString == null) {
            throw new IllegalArgumentException(index.getIndexId() + "total score不能为空");
        }

        double totalScore;
        try {
            totalScore = Double.parseDouble(totalScoreString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(index.getIndexId() + "total score 不是数字");
        }

        if ("%".equals(index.getUnit())) {
            // 百分数
            return new LinearScoreCounter(index.getIndexId(), totalScore / 100, 0);
        } else {
            // 小数
            return new LinearScoreCounter(index.getIndexId(), totalScore, 0);
        }
    }

    private LinearScoreCounter multiply(Index index) {
        String mString = index.getAnnotations().get(ScoreAnnotations.MultiplyScoreCounter.ANNOTATION_KEY_MULTIPLIER);
        if (mString == null) {
            throw new IllegalArgumentException(index.getIndexId() + "的multiplier不能为空");
        }

        double slope;
        try {
            slope = Double.parseDouble(mString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(index.getIndexId() + "的multiplier不是数字", e);
        }

        return new LinearScoreCounter(index.getIndexId(), slope, 0);
    }

    /**
     * 暂时没有实现时，使用的假Counter
     */
    private static class DummyCounter implements IndexScoreCounter {
        static DummyCounter instance = new DummyCounter();

        @Override
        public double countScore(BrandReport brandReport, Context ctx) {
            return 0;
        }
    }
}
