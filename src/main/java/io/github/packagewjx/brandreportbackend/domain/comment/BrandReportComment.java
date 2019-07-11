package io.github.packagewjx.brandreportbackend.domain.comment;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 专家评价表对象实体
 */
@ApiModel(description = "品牌报告专家评价")
public class BrandReportComment {
    /**
     * 所属报告的Id
     */
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
}
