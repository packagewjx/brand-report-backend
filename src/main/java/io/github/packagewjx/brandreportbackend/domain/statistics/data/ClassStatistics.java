package io.github.packagewjx.brandreportbackend.domain.statistics.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(description = "基于类别类型值的统计数据，记录每个值的品牌总数", parent = BaseStatistics.class)
public class ClassStatistics extends BaseStatistics {
    /**
     * 每个取值的品牌数映射表
     */
    @ApiModelProperty("每个取值的品牌数映射表")
    private Map<?, Integer> counts;

    public Map<?, Integer> getCounts() {
        return counts;
    }

    public void setCounts(Map<?, Integer> counts) {
        this.counts = counts;
    }
}
