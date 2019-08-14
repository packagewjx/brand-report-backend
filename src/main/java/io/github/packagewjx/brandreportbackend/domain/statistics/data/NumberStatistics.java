package io.github.packagewjx.brandreportbackend.domain.statistics.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "数字类型指标统计数据", parent = BaseStatistics.class)
public class NumberStatistics extends BaseStatistics {
    /**
     * 总值
     */
    @ApiModelProperty("总值")
    private Double sum;

    /**
     * 均值
     */
    @ApiModelProperty("均值")
    private Double average;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
