package io.github.packagewjx.brandreportbackend.domain.meta;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-9-11
 **/
@ApiModel(description = "表示数字类型指标的值域", parent = BaseRange.class)
@TypeAlias(NumericRange.TYPE)
public class NumericRange extends BaseRange {
    public static final String TYPE = "NumericRange";

    @ApiModelProperty("最小值")
    @NonNull
    private Double min;

    @ApiModelProperty("最大值")
    @NonNull
    private Double max;

    @ApiModelProperty("步进")
    @Nullable
    private Double step;

    public NumericRange() {
        super(TYPE);
        min = Double.NEGATIVE_INFINITY;
        max = Double.POSITIVE_INFINITY;
        step = null;
    }

    public NumericRange(Double min, Double max, Double step) {
        super(TYPE);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getStep() {
        return step;
    }

    public void setStep(Double step) {
        this.step = step;
    }
}
