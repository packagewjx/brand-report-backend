package io.github.packagewjx.brandreportbackend.service.report.score.scorecounter;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;

import java.util.Map;

/**
 * 保存计算时使用到的额外参数
 *
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-18
 **/
public class Context {
    public IndustryStatistics industryStatistics;

    public Map<String, Object> args;
}
