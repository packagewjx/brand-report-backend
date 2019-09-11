package io.github.packagewjx.brandreportbackend.domain.statistics.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.TypeAlias;

@ApiModel(description = "数字类型指标统计数据", parent = BaseStatistics.class)
@TypeAlias(value = "NumberStatistics")
public class NumberStatistics extends BaseStatistics {
    public static final String TYPE = "NumberStatistics";
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

    public NumberStatistics() {
        super(NumberStatistics.TYPE);
    }

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
