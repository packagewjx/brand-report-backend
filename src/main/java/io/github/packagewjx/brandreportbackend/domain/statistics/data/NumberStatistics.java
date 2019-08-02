package io.github.packagewjx.brandreportbackend.domain.statistics.data;

import io.swagger.annotations.ApiModelProperty;

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
