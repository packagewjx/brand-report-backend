package io.github.packagewjx.brandreportbackend.service.report.score;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;

/**
 * 定义计算一个指标的分数的接口
 *
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
public interface IndexScoreCounter {
    /**
     * 根据某一个指标，计算分数。
     *
     * @param brandReport 品牌报告
     * @return 分数值
     */
    int countScore(BrandReport brandReport);
}
