package io.github.packagewjx.brandreportbackend.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

@ApiModel(description = "品牌")
public class Brand {
    /**
     * 品牌Id
     */
    @Id
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

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}