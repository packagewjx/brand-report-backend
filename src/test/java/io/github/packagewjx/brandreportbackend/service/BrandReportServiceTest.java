package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Random;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public class BrandReportServiceTest extends BaseTest {
    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandReportService brandReportService;

    @Test
    public void buildReport() {
        int brandIndex = new Random(new Date().getTime()).nextInt(10);
        Iterable<Brand> all = brandService.getAll();
        Brand brand = null;
        assert brandIndex >= 0;
        for (int i = 0; i <= brandIndex; i++) {
            brand = all.iterator().next();
        }
        Assert.assertNotNull(brand);

        BrandReport report = brandReportService.buildReport(brand.getBrandId(), 2018, Constants.PERIOD_ANNUAL, null);
        Assert.assertNotNull(report.getData());
        Assert.assertNotEquals(0, report.getData().size());
    }
}
