package io.github.packagewjx.brandreportbackend.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

/**
 * 品牌报告类
 */
@ApiModel(description = "品牌报告类。存放某个品牌一年的报告")
public class BrandReport {
    /**
     * 报告Id
     */
    @ApiModelProperty("报告Id")
    @Id
    private String reportId;

    /**
     * 所属品牌Id
     */
    @ApiModelProperty(value = "品牌Id", required = true)
    private String brandId;

    /**
     * 报告年份
     */
    @ApiModelProperty(value = "报告年份", required = true)
    private Integer year;

    /**
     * 报告季度
     */
    @ApiModelProperty("报告季度")
    private Integer quarter;

    /**
     * 报告月份
     */
    @ApiModelProperty("报告月份")
    private Integer month;

    /**
     * 报告数据时长
     * <p>
     * 区分月度报告、季度报告还是年度报告，取值使用Constants类中以PERIOD_开头的字符串常量
     */
    @ApiModelProperty(value = "报告数据时长，区分月度报告、季度报告还是年度报告", allowableValues = "annual, month, quarter", required = true)
    private String period;

    /**
     * 报告创建时间
     */
    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

    /**
     * 报告数据
     * <p>
     * 字符串与Object的Map，字符串是IndexId，而Object则是具体的数据。根据具体的Index得知Object的类型
     */
    @ApiModelProperty(value = "报告数据", required = true)
    private Map<String, Object> data;


    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
