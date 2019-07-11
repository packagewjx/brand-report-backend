package io.github.packagewjx.brandreportbackend.domain;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 行业报告
 */
@ApiModel(description = "行业报告")
public class IndustryReport {
    /**
     * 行业名
     */
    @ApiModelProperty("行业名")
    private String industry;

    /**
     * 报告年份
     */
    @ApiModelProperty(value = "报告年份", required = true)
    private Integer year;

    /**
     * 报告月份
     */
    @ApiModelProperty(value = "报告月份", allowableValues = "[1,12]")
    private Integer month;

    /**
     * 报告季度
     */
    @ApiModelProperty(value = "报告季度", allowableValues = "[1,4]")
    private Integer quarter;

    /**
     * 报告统计时长
     * <p>
     * 取值为Constants类中的以PERIOD_开头的字符串常量
     *
     * @see Constants
     */
    @ApiModelProperty(value = "报告统计时长", required = true, allowableValues = "annual, monthly, quarterly")
    private String period;

    /**
     * 品牌Id与品牌报告的映射表
     * <p>
     * 用于获取具体的品牌数据
     */
    @ApiModelProperty("品牌报告列表")
    private Map<String, BrandReport> brandReports;

    /**
     * 行业统计数据
     */
    @ApiModelProperty("行业统计数据")
    private IndustryStatistics stat;
}
