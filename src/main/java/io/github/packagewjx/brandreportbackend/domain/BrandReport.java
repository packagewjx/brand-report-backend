package io.github.packagewjx.brandreportbackend.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

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
     * 在一年中，统计时长划分的各个不同统计时间。本报告所属统计时间的序号
     * <p>
     * 若统计时长是月份，则总共12个统计时间，取值范围为[1,12]。若是季度，则取值范围为[1,4]。若是年份，则忽略本字段。
     */
    @ApiModelProperty(value = "年内统计时间序号")
    private Integer periodTimeNumber;
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

    public Integer getPeriodTimeNumber() {
        return periodTimeNumber;
    }

    public void setPeriodTimeNumber(Integer periodTimeNumber) {
        this.periodTimeNumber = periodTimeNumber;
    }

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
