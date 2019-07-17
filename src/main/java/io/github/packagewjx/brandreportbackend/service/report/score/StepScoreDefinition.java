package io.github.packagewjx.brandreportbackend.service.report.score;

import java.util.List;

/**
 * 阶梯式分数定义类
 *
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
public class StepScoreDefinition {
    /**
     * 划分数字区间的数字数组
     * <p>
     * 根据本数组，划分的区间为：[负无穷，第一个元素), [第一个元素, 第二个元素), ... , [最后一个元素，正无穷]
     */
    private List<Double> intervalSplit;

    /**
     * 各个区间对应的分数值，大小应该是intervalSplit的大小加1。注意，这个分数值将会是使用intervalSplit划分出来的区间中，
     * 按顺序赋予区间分数，第一个区间赋予第一个分数，与intervalSplit的顺序无关。
     */
    private List<Integer> intervalScore;

    public List<Double> getIntervalSplit() {
        return intervalSplit;
    }

    public void setIntervalSplit(List<Double> intervalSplit) {
        this.intervalSplit = intervalSplit;
    }

    public List<Integer> getIntervalScore() {
        return intervalScore;
    }

    public void setIntervalScore(List<Integer> intervalScore) {
        this.intervalScore = intervalScore;
    }
}
