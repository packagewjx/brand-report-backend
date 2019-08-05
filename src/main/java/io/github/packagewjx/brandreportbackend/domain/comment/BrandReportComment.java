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
     * 评价对象ID
     */
    @Id
    @ApiModelProperty("评价对象ID")
    private String commentId;

    /**
     * 所属报告的Id
     */
    @ApiModelProperty("所属报告的Id")
    private String brandReportId;

    /**
     * 编写评价的用户Id
     */
    @ApiModelProperty("编写评价的用户ID")
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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BrandReportComment that = (BrandReportComment) o;

        return commentId.equals(that.commentId);
    }

    @Override
    public int hashCode() {
        return commentId.hashCode();
    }

    @Override
    public String toString() {
        return "BrandReportComment{" +
                "commentId='" + commentId + '\'' +
                ", brandReportId='" + brandReportId + '\'' +
                ", userId='" + userId + '\'' +
                ", overallComment='" + overallComment + '\'' +
                ", dataComment=" + dataComment +
                '}';
    }

    public String getBrandReportId() {
        return brandReportId;
    }

    public void setBrandReportId(String brandReportId) {
        this.brandReportId = brandReportId;
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
