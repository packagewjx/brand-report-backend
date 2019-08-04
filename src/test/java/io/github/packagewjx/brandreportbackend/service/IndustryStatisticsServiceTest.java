package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-4
 **/
public class IndustryStatisticsServiceTest extends BaseTest {
    @Autowired
    IndustryStatisticsService service;

    @Test
    public void count() {
        IndustryStatistics statistics = service.countStatistics("家电", 2018, "annual", 1);
        Assert.assertNotNull(statistics);
        Assert.assertEquals("家电", statistics.getIndustry());
        Assert.assertEquals((Integer) 2018, statistics.getYear());
        Assert.assertEquals("annual", statistics.getPeriod());
        Assert.assertNotNull(statistics.getStats());
        Assert.assertNotEquals(0, statistics.getStats().size());
    }
}
