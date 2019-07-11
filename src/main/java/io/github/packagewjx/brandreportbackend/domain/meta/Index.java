package io.github.packagewjx.brandreportbackend.domain.meta;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 指标类。
 * <p>
 * 表示品牌报告的的评价指标
 */
@ApiModel(description = "指标类。表示品牌报告的的评价指标")
public class Index {
    /**
     * 数字类型数据
     */
    public static final String TYPE_NUMBER = "number";
    /**
     * 字符串类型数据
     */
    public static final String TYPE_STRING = "string";
    /**
     * 布尔类型数据
     */
    public static final String TYPE_BOOL = "bool";


    /**
     * 指标的英文ID
     * <p>
     * 通常是使用英文连字符连接起来的一串有意义的文字。每一份报告中，每一个指标将引用这个ID作为键。
     */
    @ApiModelProperty(value = "指标的英文ID。通常是使用连字符连接的表示指标内容的英文")
    private String indexId;

    /**
     * 指标的显示名称
     */
    @ApiModelProperty(value = "指标的显示名称。用于在展现时供用户查看")
    private String displayName;

    /**
     * 本指标的上级指标的indexId
     * <p>
     * 用于表示指标的层级关系，引用本指标的上级指标的indexId。若本字段为null，表示这是根指标
     */
    @ApiModelProperty(value = "本指标的上级指标的indexId")
    private String parentIndexId;

    /**
     * 指标具体数据的类型。使用Index类中的以`TYPE`开头的常量
     */
    @ApiModelProperty(value = "本指标数据的具体类型", allowableValues = "number, string, bool", required = true)
    private String type;

    /**
     * 数据统计时长
     * <p>
     * 具体取值放置在Constants类中，以PERIOD_开头的常量
     *
     * @see io.github.packagewjx.brandreportbackend.domain.Constants
     */
    @ApiModelProperty(value = "数据统计时长", allowableValues = "annual, monthly, quarterly")
    private String period;

    /**
     * 指标的详细描述
     */
    @ApiModelProperty("指标的详细描述")
    private String description;

    /**
     * 指标的数值单位
     */
    @ApiModelProperty("指标的数值单位")
    private String unit;

    /**
     * 额外的注解
     * <p>
     * 用于系统处理本指标数据时存放自定义设置
     */
    @ApiModelProperty(value = "指标注解。用于存放自定义的额外数据，以供其他系统（统计系统、计分系统）处理")
    private Map<String, String> annotations;
}
