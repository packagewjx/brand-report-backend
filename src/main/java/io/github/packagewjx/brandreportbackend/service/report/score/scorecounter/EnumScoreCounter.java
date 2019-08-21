package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.report.score.EnumScoreDefinition;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-19
 **/
public class EnumScoreCounter implements IndexScoreCounter {
    private static final Logger logger = LoggerFactory.getLogger(EnumScoreCounter.class);
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

        logger.info("指标{}使用枚举值计分器，分数定义为{}", indexId, definition.getDefinition());
    }

    @Override
    public double countScore(BrandReport brandReport, Context ctx) {
        if (brandReport == null || brandReport.getData() == null) {
            throw new IllegalArgumentException("数据为空");
        }

        Object val = brandReport.getData().get(indexId);
        if (val == null) {
            logger.trace("品牌报告(ID:{})的{}指标的值为null，分数为0", brandReport.getReportId(), indexId);
            return 0;
        }
        // 使用toString是因为数据库中的json不能使用数字作为键，但是val可能是数字，使用toString会转为数字对应的字符串
        Double score = definition.getDefinition().get(val.toString());
        if (score == null) {
            logger.error("品牌报告(ID:{})的{}指标的值为{}，没有对应的分数值", brandReport.getReportId(), indexId, val);
            return 0;
        } else {
            logger.trace("品牌报告(ID:{})的{}指标的值为{}，分数为{}", brandReport.getReportId(), indexId, val, score);
            return score;
        }
    }
}
