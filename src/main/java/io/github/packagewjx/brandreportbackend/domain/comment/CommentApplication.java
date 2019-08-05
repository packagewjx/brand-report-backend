package io.github.packagewjx.brandreportbackend.domain.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.util.Collection;
import java.util.Date;

@ApiModel(description = "专家评价申请")
public class CommentApplication {
    /**
     * 状态：已申请评价
     */
    public static final String STATE_APPLIED = "applied";

    /**
     * 状态：评价中
     */
    public static final String STATE_COMMENTING = "commenting";

    /**
     * 状态：已完成评价
     */
    public static final String STATE_FINISHED = "finished";

    /**
     * 评价申请ID
     */
    @Id
    @ApiModelProperty("评价申请ID")
    private String applicationId;
    /**
     * 所需要评价的报告Id
     */
    @ApiModelProperty("所需要评价的报告Id")
    private String brandReportId;
    /**
     * 申请人用户Id
     */
    @ApiModelProperty("申请人用户Id")
    private String applicantId;
    /**
     * 申请的专家ID
     */
    @ApiModelProperty("请求的专家用户ID")
    private Collection<String> expertUserIds;
    /**
     * 申请状态
     * <p>
     * 当申请建立的时候，进入“已申请评价”状态。当专家开始查看并且作出评价，但是没有评价完毕的时候，则进入“评价中”状态。
     * 当所有专家都添加了评价之后，进入“完成评价”状态。
     */
    @ApiModelProperty(value = "申请状态", allowableValues = "applied, commenting, finished")
    private String state;
    /**
     * 状态更新日期
     */
    @ApiModelProperty(value = "状态更新日期")
    private Date stateUpdate;

    @Override
    public String toString() {
        return "CommentApplication{" +
                "applicationId='" + applicationId + '\'' +
                ", brandReportId='" + brandReportId + '\'' +
                ", applicantId='" + applicantId + '\'' +
                ", expertUserIds=" + expertUserIds +
                ", state='" + state + '\'' +
                ", stateUpdate=" + stateUpdate +
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

        CommentApplication that = (CommentApplication) o;

        return applicationId.equals(that.applicationId);
    }

    @Override
    public int hashCode() {
        return applicationId.hashCode();
    }

    public Date getStateUpdate() {
        return stateUpdate;
    }

    public void setStateUpdate(Date stateUpdate) {
        this.stateUpdate = stateUpdate;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getBrandReportId() {
        return brandReportId;
    }

    public void setBrandReportId(String brandReportId) {
        this.brandReportId = brandReportId;
    }

    public Collection<String> getExpertUserIds() {
        return expertUserIds;
    }

    public void setExpertUserIds(Collection<String> expertUserIds) {
        this.expertUserIds = expertUserIds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
