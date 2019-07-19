package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;

import java.util.Map;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-18
 **/
public class BoolScoreCounter implements IndexScoreCounter {
    public static final double DEFAULT_TRUE_SCORE = 100;
    public static final double DEFAULT_FALSE_SCORE = 0;

    private String indexId;
    private double trueScore;
    private double falseScore;

    public BoolScoreCounter(Index index) {
        if (index == null) {
            throw new IllegalArgumentException("Index不能为空");
        }

        Map<String, String> annotations = index.getAnnotations();
        if (annotations == null || annotations.size() == 0) {
            throw new IllegalArgumentException("Index的annotations不能为空");
        }

        String val;
        if ((val = annotations.get(ScoreAnnotations.BoolScoreCounter.ANNOTATION_KEY_TRUE_SCORE)) == null) {
            this.trueScore = DEFAULT_TRUE_SCORE;
        } else {
            try {
                this.trueScore = Double.parseDouble(val);
            } catch (NumberFormatException e) {
                this.trueScore = DEFAULT_TRUE_SCORE;
            }
        }

        if ((val = annotations.get(ScoreAnnotations.BoolScoreCounter.ANNOTATION_KEY_FALSE_SCORE)) == null) {
            this.falseScore = DEFAULT_FALSE_SCORE;
        } else {
            try {
                this.falseScore = Double.parseDouble(val);
            } catch (NumberFormatException e) {
                this.falseScore = DEFAULT_FALSE_SCORE;
            }
        }

        this.indexId = index.getIndexId();
    }


    /**
     * 算分规则：
     * <ol>
     * <li>如果为null，返回false的分数</li>
     * <li>如果是布尔值，则根据值返回分数</li>
     * <li>如果是数字，则若值为0，返回false的分数，否则返回true的分数</li>
     * <li>到这步，就确定是一个对象，但不知其类型，又不是null，因而返回true的分数</li>
     * </ol>
     *
     * @param brandReport 品牌报告
     * @param ctx         ctx
     * @return 分数
     */
    @Override
    public double countScore(BrandReport brandReport, Context ctx) {
        Object val = brandReport.getData().get(indexId);
        if (val == null) {
            return this.falseScore;
        }
        if (val instanceof Boolean) {
            return ((Boolean) val) ? trueScore : falseScore;
        } else if (val instanceof Number) {
            // 若是数字，则当为0的时候返回false，其余返回true
            return ((Number) val).doubleValue() == 0 ? falseScore : trueScore;
        } else {
            // 是普通对象，且不是null，返回true的分数
            return this.trueScore;
        }
    }
}
