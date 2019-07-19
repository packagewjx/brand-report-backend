package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.report.score.EnumScoreDefinition;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;

import java.io.IOException;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-19
 **/
public class EnumScoreCounter implements IndexScoreCounter {
    private String indexId;
    private EnumScoreDefinition definition;

    public EnumScoreCounter(Index index) {
        if (index == null || index.getAnnotations() == null) {
            throw new IllegalArgumentException("Index及注解不能为空");
        }
        indexId = index.getIndexId();
        if (!ScoreAnnotations.EnumScoreCounter.ANNOTATION_VALUE_TYPE.equals(index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE))) {
            throw new IllegalArgumentException(indexId + "不适用本类进行计算");
        }
        String defString = index.getAnnotations().get(ScoreAnnotations.EnumScoreCounter.ANNOTATION_KEY_ENUM_SCORE);
        if (defString == null) {
            throw new IllegalArgumentException(indexId + "的分数定义为空！");
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            definition = mapper.readValue(defString, EnumScoreDefinition.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(indexId + "的json解析错误", e);
        }
        if (definition.getDefinition() == null || definition.getDefinition().size() == 0) {
            throw new IllegalArgumentException(indexId + "的分数定义为空！");
        }
    }

    @Override
    public double countScore(BrandReport brandReport, Context ctx) {
        if (brandReport == null || brandReport.getData() == null) {
            throw new IllegalArgumentException("数据为空");
        }

        Object val = brandReport.getData().get(indexId);
        if (val == null) {
            return 0;
        }
        // 使用toString是因为数据库中的json不能使用数字作为键，但是val可能是数字，使用toString会转为数字对应的字符串
        Double score = definition.getDefinition().get(val.toString());
        if (score == null) {
            // FIXME 使用日志输出
            System.out.println(indexId + "的" + val + "没有对应的分数值");
            return 0;
        } else {
            return score;
        }
    }
}
