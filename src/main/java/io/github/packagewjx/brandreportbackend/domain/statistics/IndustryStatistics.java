package io.github.packagewjx.brandreportbackend.domain.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 行业统计数据
 */
@ApiModel(description = "行业统计数据")
public class IndustryStatistics {
    /**
     * Id
     */
    @ApiModelProperty("Id")
    private String statId;

    /**
     * 行业Id
     */
    @ApiModelProperty("行业Id")
    private String industryId;

    /**
     * 统计数据年份
     */
    @ApiModelProperty("统计数据年份")
    private Integer year;

    /**
     * 统计数据月份
     */
    @ApiModelProperty("统计数据月份")
    private Integer month;

    /**
     * 统计数据季度
     */
    @ApiModelProperty("统计数据季度")
    private Integer quarter;

    /**
     * 行业品牌总数
     * <p>
     * 参与统计的品牌总数
     */
    @ApiModelProperty("行业品牌总数")
    private Integer total;

    /**
     * 统计数据
     */
    @ApiModelProperty("统计数据")
    private Map<String, BaseStatistics> stats;
}
