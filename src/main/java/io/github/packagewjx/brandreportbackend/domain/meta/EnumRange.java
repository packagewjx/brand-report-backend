package io.github.packagewjx.brandreportbackend.domain.meta;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-9-11
 **/
@ApiModel(description = "表示枚举类型值的取值范围", parent = BaseRange.class)
@TypeAlias(EnumRange.TYPE)
public class EnumRange extends BaseRange {
    public static final String TYPE = "EnumRange";

    @ApiModelProperty("允许取值的集合")
    @NonNull
    private Set<Object> allowableValues;

    public EnumRange() {
        super(TYPE);
        allowableValues = new HashSet<>();
    }

    public EnumRange(Set<Object> allowableValues) {
        super(TYPE);
        this.allowableValues = allowableValues;
    }

    public Set<Object> getAllowableValues() {
        return allowableValues;
    }

    public void setAllowableValues(Set<Object> allowableValues) {
        this.allowableValues = allowableValues;
    }
}
