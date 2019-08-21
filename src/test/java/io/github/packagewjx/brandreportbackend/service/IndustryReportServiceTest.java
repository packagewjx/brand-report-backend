package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.IndustryReport;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
public class IndustryReportServiceTest extends BaseTest {
    @Autowired
    IndustryReportService service;

    @Test
    public void buildReport() {
        IndustryReport report = service.buildIndustryReport("家电", 2018, Constants.PERIOD_ANNUAL, 1);
        Assert.assertNotNull(report);
        Assert.assertEquals("家电", report.getIndustry());
        Assert.assertEquals((Integer) 2018, report.getYear());
        Assert.assertEquals(Constants.PERIOD_ANNUAL, report.getPeriod());
        Map<String, BrandReport> brandReports = report.getBrandReports();
        Assert.assertNotNull(brandReports);
        Assert.assertNotNull(report.getStat());
        Assert.assertNotEquals(0, brandReports.size());
        Assert.assertEquals("家电", report.getStat().getIndustry());
        Assert.assertNotNull(report.getStat().getStats());
        report.getStat().getStats().forEach((s, baseStatistics) -> Assert.assertNotNull(baseStatistics));

        brandReports.forEach((s, brandReport) -> {
            Assert.assertEquals(s, brandReport.getBrandId());
            Assert.assertNotNull(brandReport.getData());
            Assert.assertNotEquals(0, brandReport.getData().size());
            Assert.assertEquals((Integer) 2018, brandReport.getYear());
            Assert.assertEquals(Constants.PERIOD_ANNUAL, brandReport.getPeriod());
            brandReport.getData().forEach((s1, o) -> Assert.assertNotNull(o));
        });
    }

    @Test
    public void saveGetDeleteOne() {
        IndustryReport report = service.buildIndustryReport("家电", 2018, Constants.PERIOD_ANNUAL, 0);
        report = service.save(report);
        Assert.assertNotNull(report.getIndustryReportId());

        Optional<IndustryReport> byId = service.getById(report.getIndustryReportId());
        Assert.assertTrue(byId.isPresent());
        report = byId.get();
        Assert.assertEquals("家电", report.getIndustry());
        Assert.assertEquals((Integer) 2018, report.getYear());
        Assert.assertNotNull(report.getBrandReports());
        Assert.assertNotEquals(0, report.getBrandReports());
        Assert.assertNotNull(report.getStat());

        service.delete(report);
        byId = service.getById(report.getIndustryReportId());
        Assert.assertFalse(byId.isPresent());
    }


}
