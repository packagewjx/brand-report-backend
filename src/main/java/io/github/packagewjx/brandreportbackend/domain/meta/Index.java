package io.github.packagewjx.brandreportbackend.domain.meta;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

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
     * 枚举类型
     */
    public static final String TYPE_ENUM = "enum";
    /**
     * 指标的集合，也就是处在指标层次结构中间节点的指标
     */
    public static final String TYPE_INDICES = "indices";
    /**
     * 指标的英文ID
     * <p>
     * 通常是使用英文连字符连接起来的一串有意义的文字。每一份报告中，每一个指标将引用这个ID作为键。
     * <p>
     * 起id规范：
     * <ol>
     * <li>若是拥有确定的统计时间，且将会有多个不同的统计周期指标时，
     * 则使用时间周期开头，如annual，monthly，quarterly开头。</li>
     * <li>若是上级指标的子指标，如果可能，指标中需要由上层指标的名称放前面，
     * 如：上层指标为parent，本指标为index，则起名为parent-index</li>
     * <li>使用有意义的英文，多个英文之间使用连字符分隔开</li>
     * </ol>
     */
    @Id
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
    @ApiModelProperty(value = "本指标数据的具体类型", allowableValues = "number, string, bool, enum, indices", required = true)
    private String type;
    /**
     * 数据统计时长
     * <p>
     * 具体取值放置在Constants类中，以PERIOD_开头的常量。若是default，意味着本指标的统计周期与数据集和报告的统计周期一致
     *
     * @see io.github.packagewjx.brandreportbackend.domain.Constants
     */
    @ApiModelProperty(value = "数据统计时长", allowableValues = "annual, monthly, quarterly, default")
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
     * 额外的注解，用于系统处理本指标数据时的自定义设置
     * <p>
     * 注解的命名约定：
     * <ul>
     * <li>使用小写字母，使用连字符分隔单词</li>
     * <li>为了避免重复，以及区分注解使用的子系统，注解的命名应该类似包名，使用下划线(_)组成注解名的层次结构。注解层次应该体现注解的使用位置</li>
     * </ul>
     *
     * <p>
     */
    @ApiModelProperty(value = "指标注解。用于存放自定义的额外数据，以供其他系统（统计系统、计分系统）处理")
    private Map<String, String> annotations;

    @Override
    public String toString() {
        return "Index{" +
                "indexId='" + indexId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", parentIndexId='" + parentIndexId + '\'' +
                ", type='" + type + '\'' +
                ", period='" + period + '\'' +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                ", annotations=" + annotations +
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

        Index index = (Index) o;

        return indexId.equals(index.indexId);
    }

    @Override
    public int hashCode() {
        return indexId.hashCode();
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getParentIndexId() {
        return parentIndexId;
    }

    public void setParentIndexId(String parentIndexId) {
        this.parentIndexId = parentIndexId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }
}
