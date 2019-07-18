package io.github.packagewjx.brandreportbackend.service.report.score;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
public class ScoreAnnotations {
    public static final String ANNOTATION_KEY_TYPE = "score_type";

    private static final class BoolScoreCounter {
        public static final String ANNOTATION_VALUE_TYPE = "bool";
        /**
         * 值为true时得到的分数
         */
        public static final String ANNOTATION_KEY_TRUE_SCORE = "score_bool_true-score";
        /**
         * 值为false时得到的分数
         */
        public static final String ANNOTATION_KEY_FALSE_SCORE = "score_bool_false-score";
    }

    private static final class EnumScoreCounter {
        public static final String ANNOTATION_VALUE_TYPE = "enum";

        /**
         * 分数的定义。值需要是一个json，键是enum的各个取值，对应的值则是该取值的分数
         */
        public static final String ANNOTATION_KEY_ENUM_SCORE = "score_enum_score-definition";
    }

    public static final class RatioScoreCounter {
        public static final String ANNOTATION_VALUE_TYPE = "ratio";

        /**
         * 总分，最终得分是总分乘以比值
         */
        public static final String ANNOTATION_KEY_TOTAL_SCORE = "score_ratio_total-score";
    }

    public static final class StepScoreCounter {
        public static final String ANNOTATION_VALUE_TYPE = "step";

        /**
         * 阶梯式分数的定义。值为json，类型参考StepScoreDefinition
         */
        public static final String ANNOTATION_KEY_SCORE_DEFINITION = "score_step_score-definition";
    }

    public static final class MultiplyScoreCounter {
        public static final String ANNOTATION_VALUE_TYPE = "multiply";

        /**
         * 乘法基准分。最终得分是指标的值乘以这个基准分
         */
        public static final String ANNOTATION_KEY_MULTIPLIER = "score_multiply_multiplier";
    }

    public static final class ScoreRatioScoreCounter {
        public static final String ANNOTATION_VALUE_TYPE = "score-ratio";

        /**
         * 比例型分数的满分，若值是1，则拿满分，否则按比例乘以总分得到
         */
        public static final String ANNOTATION_KEY_SCORE_RATIO_TOTAL_SCORE = "score_score_ratio_total_score";

    }

}
