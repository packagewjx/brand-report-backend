package io.github.packagewjx.brandreportbackend.domain.meta;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-9-11
 **/
@JsonTypeInfo(include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericRange.class, name = NumericRange.TYPE),
        @JsonSubTypes.Type(value = EnumRange.class, name = EnumRange.TYPE)
})
@ApiModel(description = "表示指标的值域", subTypes = {NumericRange.class})
public abstract class BaseRange {
    @Field("_class")
    @ApiModelProperty(value = "具体值域类型", allowableValues = "NumberRange, EnumRange")
    private String type;

    protected BaseRange(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
