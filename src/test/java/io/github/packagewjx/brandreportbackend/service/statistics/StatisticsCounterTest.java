package io.github.packagewjx.brandreportbackend.service.statistics;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
public class StatisticsCounterTest extends BaseTest {
    @Autowired
    private StatisticsCounter counter;

    @Test
    public void count() {
        IndustryStatistics stat = counter.count("家电", 2018, 0, 0, Constants.PERIOD_ANNUAL);
        Assert.assertNotNull(stat);
        Assert.assertNotNull(stat.getStats());
        Assert.assertEquals("家电", stat.getIndustry());
        Assert.assertEquals((Integer) 2018, stat.getYear());
        Assert.assertEquals(Constants.PERIOD_ANNUAL, stat.getPeriod());
        Assert.assertNotEquals((Integer) 0, stat.getTotal());

        // 检查统计的数据
        Assert.assertNotEquals(0, stat.getStats());
        stat.getStats().forEach((key, value) -> Assert.assertNotNull(value));
    }
}
