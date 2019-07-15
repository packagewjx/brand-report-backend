package io.github.packagewjx.brandreportbackend.domain.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * 行业统计数据
 */
@ApiModel(description = "行业统计数据")
public class IndustryStatistics {
    /**
     * Id
     */
    @Id
    @ApiModelProperty("Id")
    private String statId;

    /**
     * 行业Id
     */
    @ApiModelProperty("行业Id")
    private String industryId;

    /**
     * 统计数据年份
     */
    @ApiModelProperty("统计数据年份")
    private Integer year;

    /**
     * 统计数据月份
     */
    @ApiModelProperty("统计数据月份")
    private Integer month;

    /**
     * 统计数据季度
     */
    @ApiModelProperty("统计数据季度")
    private Integer quarter;

    /**
     * 行业品牌总数
     * <p>
     * 参与统计的品牌总数
     */
    @ApiModelProperty("行业品牌总数")
    private Integer total;

    /**
     * 统计数据
     */
    @ApiModelProperty("统计数据")
    private Map<String, BaseStatistics> stats;

    public String getStatId() {
        return statId;
    }

    public void setStatId(String statId) {
        this.statId = statId;
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Map<String, BaseStatistics> getStats() {
        return stats;
    }

    public void setStats(Map<String, BaseStatistics> stats) {
        this.stats = stats;
    }
}
