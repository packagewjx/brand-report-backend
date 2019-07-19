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
        } else {
            return DummyCounter.instance;
        }
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
