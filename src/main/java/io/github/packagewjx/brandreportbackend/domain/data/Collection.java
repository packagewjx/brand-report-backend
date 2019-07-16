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
     * 数据所属季度
     */
    @ApiModelProperty(value = "数据所属季度", allowableValues = "[1,4]")
    private Integer quarter;

    /**
     * 数据所属月份
     */
    @ApiModelProperty(value = "数据所属月份", allowableValues = "[1,12]")
    private Integer month;

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

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
