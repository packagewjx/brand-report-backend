package io.github.packagewjx.brandreportbackend.domain;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

/**
 * 行业报告
 */
@ApiModel(description = "行业报告")
public class IndustryReport {
    /**
     * 行业报告ID
     */
    @Id
    @ApiModelProperty
    private String industryReportId;
    /**
     * 行业名
     */
    @ApiModelProperty("行业名")
    private String industry;
    /**
     * 报告年份
     */
    @ApiModelProperty(value = "报告年份", required = true)
    private Integer year;
    /**
     * 年内报告统计时间序号
     */
    @ApiModelProperty(value = "年内报告统计时间序号")
    private Integer periodTimeNumber;
    /**
     * 报告统计时长
     * <p>
     * 取值为Constants类中的以PERIOD_开头的字符串常量
     *
     * @see Constants
     */
    @ApiModelProperty(value = "报告统计时长", required = true, allowableValues = "annual, monthly, quarterly")
    private String period;
    /**
     * 品牌Id与品牌报告的映射表
     * <p>
     * 用于获取具体的品牌数据
     */
    @ApiModelProperty("品牌报告列表")
    private Map<String, BrandReport> brandReports;
    /**
     * 行业统计数据
     */
    @ApiModelProperty("行业统计数据")
    private IndustryStatistics stat;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Override
    public String toString() {
        return "IndustryReport{" +
                "industryReportId='" + industryReportId + '\'' +
                ", industry='" + industry + '\'' +
                ", year=" + year +
                ", periodTimeNumber=" + periodTimeNumber +
                ", period='" + period + '\'' +
                ", brandReports=" + brandReports +
                ", stat=" + stat +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndustryReport report = (IndustryReport) o;

        return industryReportId.equals(report.industryReportId);
    }

    @Override
    public int hashCode() {
        return industryReportId.hashCode();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIndustryReportId() {
        return industryReportId;
    }

    public void setIndustryReportId(String industryReportId) {
        this.industryReportId = industryReportId;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Map<String, BrandReport> getBrandReports() {
        return brandReports;
    }

    public void setBrandReports(Map<String, BrandReport> brandReports) {
        this.brandReports = brandReports;
    }

    public IndustryStatistics getStat() {
        return stat;
    }

    public void setStat(IndustryStatistics stat) {
        this.stat = stat;
    }
}
