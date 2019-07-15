package io.github.packagewjx.brandreportbackend.domain.comment;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * 专家评价表对象实体
 */
@ApiModel(description = "品牌报告专家评价")
public class BrandReportComment {
    /**
     * 所属报告的Id
     */
    @Id
    @ApiModelProperty("所属报告的Id")
    private String reportId;

    /**
     * 编写评价的用户Id
     */
    private String userId;

    /**
     * 报告整体评价
     */
    @ApiModelProperty("报告整体评价")
    private String overallComment;

    /**
     * 对具体数据的评价
     */
    @ApiModelProperty("对具体数据的评价")
    private Map<String, String> dataComment;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOverallComment() {
        return overallComment;
    }

    public void setOverallComment(String overallComment) {
        this.overallComment = overallComment;
    }

    public Map<String, String> getDataComment() {
        return dataComment;
    }

    public void setDataComment(Map<String, String> dataComment) {
        this.dataComment = dataComment;
    }
}
