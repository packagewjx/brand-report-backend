package io.github.packagewjx.brandreportbackend.domain.statistics.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassStatistics.class, name = "ClassStatistics"),
        @JsonSubTypes.Type(value = NumberStatistics.class, name = "NumberStatistics"),
})
public abstract class BaseStatistics {

}
