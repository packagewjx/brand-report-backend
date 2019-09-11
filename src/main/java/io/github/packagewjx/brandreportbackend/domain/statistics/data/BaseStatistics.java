package io.github.packagewjx.brandreportbackend.domain.statistics.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.mapping.Field;
import springfox.documentation.annotations.ApiIgnore;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassStatistics.class, name = ClassStatistics.TYPE),
        @JsonSubTypes.Type(value = NumberStatistics.class, name = NumberStatistics.TYPE),
})
@ApiIgnore("统计数据基类")
public abstract class BaseStatistics {
    /**
     * 为了节省空间，使用_class来记录。要确保@TypeAlias和这个JsonSubTypes是同一个名字，一个用于SpringDataMongo，一个用于
     * Jackson反序列化
     */
    @Field("_class")
    @ApiModelProperty(required = true, value = "说明本统计字段是什么类型的，用于反序列化",
            allowableValues = "NumberStatistics, ClassStatistics")
    private String type;

    protected BaseStatistics(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
