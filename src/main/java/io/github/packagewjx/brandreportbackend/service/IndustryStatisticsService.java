package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public interface IndustryStatisticsService extends BaseService<IndustryStatistics, String> {
    /**
     * 统计本系统中的行业信息
     * <p>
     * 本方法会创建新的行业统计信息，从本系统中的数据中进行统计。得到的新统计信息不会自动保存
     * <p>
     * 由于分数的计算需要先获取行业统计数据，因此本函数会在分数计算之前调用，因而无法统计分数信息。
     *
     * @param period           统计时长
     * @param industry         行业
     * @param periodTimeNumber 年内统计时间
     * @param year             年份
     * @return 使用本系统统计的行业信息
     */
    IndustryStatistics countStatistics(@NonNull String industry, @NonNull Integer year, @NonNull String period, @Nullable Integer periodTimeNumber);

    /**
     * 执行分数统计
     *
     * @param industry         行业
     * @param year             年份
     * @param period           统计时长
     * @param periodTimeNumber 年内统计时间
     * @param statistics       原统计对象，若不是null，则会将分数统计结果放入此对象中
     * @return 统计分数之后的结果
     */
    IndustryStatistics countScoreStatistics(@NonNull String industry, @NonNull Integer year, @NonNull String period, @Nullable Integer periodTimeNumber, @Nullable IndustryStatistics statistics);
}
