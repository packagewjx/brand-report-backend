package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.comment.CommentApplication;

import java.util.Collection;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
public interface CommentApplicationService extends BaseService<CommentApplication, String> {
    /**
     * 根据报告Id获取报告评价申请
     *
     * @param brandReportId 报告申请
     * @return 该报告关联的所有评价申请
     */
    Collection<CommentApplication> getByBrandReportId(String brandReportId);

    /**
     * 根据申请人Id获取其申请的所有报告评价
     *
     * @param applicantId 申请人Id
     * @return 该申请人申请的报告
     */
    Collection<CommentApplication> geyByApplicantId(String applicantId);

    /**
     * 根据专家用户ID获取其所有需要填写报告的申请
     *
     * @param expertId 专家用户ID
     * @return 所有申请
     */
    Collection<CommentApplication> getByExpertId(String expertId);
}
