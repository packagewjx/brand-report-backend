package io.github.packagewjx.brandreportbackend.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "品牌")
public class Brand {
    /**
     * 品牌Id
     */
    @ApiModelProperty(value = "品牌Id")
    private String brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称", required = true)
    private String brandName;

    /**
     * 所属行业
     */
    @ApiModelProperty(value = "所属行业", required = true)
    private String industry;
}