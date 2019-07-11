package io.github.packagewjx.brandreportbackend.domain.statistics;

import io.swagger.annotations.ApiModelProperty;

public class NumberStatistics extends BaseStatistics {
    /**
     * 总值
     */
    @ApiModelProperty("总值")
    Double sum;

    /**
     * 均值
     */
    @ApiModelProperty("均值")
    Double average;
}
