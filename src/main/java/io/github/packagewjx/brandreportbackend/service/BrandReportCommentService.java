package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.comment.BrandReportComment;

import java.util.Collection;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
public interface BrandReportCommentService extends BaseService<BrandReportComment, String> {
    /**
     * 根据品牌报告Id获取该报告的所有评价
     *
     * @param brandReportId 品牌报告Id
     * @return 该报告的所有评价
     */
    Collection<BrandReportComment> getByBrandReportId(String brandReportId);

    /**
     * 根据评价编写者Id获取评价
     *
     * @param userId 评价编写者用户ID
     * @return 评价
     */
    Collection<BrandReportComment> getByUserId(String userId);
}
