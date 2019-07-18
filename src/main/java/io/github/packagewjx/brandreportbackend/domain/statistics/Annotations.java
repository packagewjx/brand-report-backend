package io.github.packagewjx.brandreportbackend.domain.statistics;

/**
 * 统计模块所使用的Index注解
 *
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
public class Annotations {
    /**
     * 统计时不会统计这个Index。默认false
     * <p>
     * 设置为true值起作用
     */
    public static final String STAT_ANNOTATION_NO_COUNT = "statistics_no-count";

    /**
     * 统计时把null值当成是0值，计算平均值时将会计入这个null。默认是忽略null
     * <p>
     * 设置为true值起作用
     */
    public static final String STAT_ANNOTATION_NULL_AS_ZERO = "statistics_null-as-zero";
}
