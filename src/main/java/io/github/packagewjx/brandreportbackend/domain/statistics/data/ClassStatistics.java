package io.github.packagewjx.brandreportbackend.domain.statistics.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.TypeAlias;

import java.util.Map;

@ApiModel(description = "基于类别类型值的统计数据，记录每个值的品牌总数", parent = BaseStatistics.class)
@TypeAlias("ClassStatistics")
public class ClassStatistics extends BaseStatistics {
    public static final String TYPE = "ClassStatistics";
    /**
     * 每个取值的品牌数映射表
     */
    @ApiModelProperty("每个取值的品牌数映射表")
    private Map<?, Integer> counts;

    /**
     * 数据总数
     */
    @ApiModelProperty("数据总数")
    private int total;

    public ClassStatistics() {
        super(TYPE);
    }

    public Map<?, Integer> getCounts() {
        return counts;
    }

    public void setCounts(Map<?, Integer> counts) {
        this.counts = counts;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
