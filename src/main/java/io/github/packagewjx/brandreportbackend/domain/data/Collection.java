package io.github.packagewjx.brandreportbackend.domain.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * 数据类
 * <p>
 * 放置某一个品牌的某些指标的数据。
 * <p>
 * 数据一般有数据时间。对于年度数据，只读取year字段即可。季度数据，则读取year与quarter。月度数据则读取year与month即可。
 */
@ApiModel(description = "数据类")
public class Collection {
    /**
     * 数据集合Id
     */
    @Id
    @ApiModelProperty("数据集合Id")
    private String collectionId;

    /**
     * 数据所属年度
     */
    @ApiModelProperty(value = "数据所属年度", required = true)
    private Integer year;

    /**
     * 在一年中，统计时长划分的各个不同统计时间。本字段为本报告所属统计时间的序号
     * <p>
     * 若统计时长是月份，则总共12个统计时间，取值范围为[1,12]。若是季度，则取值范围为[1,4]。若是年份，则忽略本字段。
     */
    private Integer periodTimeNumber;

    /**
     * 数据统计时长
     */
    @ApiModelProperty(value = "数据统计时长", allowableValues = "annual, monthly, quarterly")
    private String period;

    /**
     * 数据所属品牌
     */
    @ApiModelProperty(value = "数据所属品牌", required = true)
    private String brandId;

    /**
     * 数据集合
     * <p>
     * 键为indexId，值为具体的数据
     */
    @ApiModelProperty(value = "报告数据。键值对，键为indexId，值为具体数据", required = true)
    private Map<String, Object> data;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPeriodTimeNumber() {
        return periodTimeNumber;
    }

    public void setPeriodTimeNumber(Integer periodTimeNumber) {
        this.periodTimeNumber = periodTimeNumber;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
