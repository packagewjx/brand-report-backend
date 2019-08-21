package io.github.packagewjx.brandreportbackend.dataobject;

import io.github.packagewjx.brandreportbackend.domain.Constants;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-21
 **/
public class IndustryReportDO {
    /**
     * 行业报告ID
     */
    @Id
    private String industryReportId;
    /**
     * 行业名
     */
    private String industry;
    /**
     * 报告年份
     */
    private Integer year;
    /**
     * 年内报告统计时间序号
     */
    private Integer periodTimeNumber;
    /**
     * 报告统计时长
     * <p>
     * 取值为Constants类中的以PERIOD_开头的字符串常量
     *
     * @see Constants
     */
    private String period;
    /**
     * 品牌报告ID表。键为品牌ID，值为品牌报告ID
     */
    private Map<String, String> brandReportIds;
    /**
     * 行业统计ID
     */
    private String industryStatisticsId;
    /**
     * 创建时间
     */
    private Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndustryReportDO reportDO = (IndustryReportDO) o;

        return industryReportId.equals(reportDO.industryReportId);
    }

    @Override
    public int hashCode() {
        return industryReportId.hashCode();
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

    public Map<String, String> getBrandReportIds() {
        return brandReportIds;
    }

    public void setBrandReportIds(Map<String, String> brandReportIds) {
        this.brandReportIds = brandReportIds;
    }

    public String getIndustryStatisticsId() {
        return industryStatisticsId;
    }

    public void setIndustryStatisticsId(String industryStatisticsId) {
        this.industryStatisticsId = industryStatisticsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
