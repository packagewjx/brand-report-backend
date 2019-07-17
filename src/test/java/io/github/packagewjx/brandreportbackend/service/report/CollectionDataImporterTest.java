package io.github.packagewjx.brandreportbackend.service.report;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.repository.BrandRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
public class CollectionDataImporterTest extends BaseTest {
    @Autowired
    CollectionDataImporter importer;

    @Autowired
    BrandRepository brandRepository;

    @Test
    public void normalImport() {
        List<Brand> allBrand = brandRepository.findAll();

        BrandReport report = new BrandReport();
        report.setBrandId(allBrand.get(0).getBrandId());
        report.setYear(2018);
        report.setPeriod(Constants.PERIOD_ANNUAL);
        report.setCreateTime(new Date());
        report.setData(new HashMap<>(64));

        importer.importData(report);

        Assert.assertNotEquals(0, report.getData().size());
    }

    @Test
    public void nullParam() {
        importer.importData(null);
    }

    @Test
    public void nullData() {
        List<Brand> allBrand = brandRepository.findAll();

        BrandReport report = new BrandReport();
        report.setBrandId(allBrand.get(0).getBrandId());
        report.setYear(2018);
        report.setPeriod(Constants.PERIOD_ANNUAL);
        report.setCreateTime(new Date());

        importer.importData(report);

        Assert.assertNotEquals(0, report.getData().size());
    }

    @Test
    public void dataNotExist() {
        List<Brand> all = brandRepository.findAll();
        BrandReport report = new BrandReport();
        report.setData(new HashMap<>(64));
        report.setCreateTime(new Date());
        report.setPeriod(Constants.PERIOD_ANNUAL);
        report.setYear(2000);
        report.setBrandId(all.get(0).getBrandId());

        importer.importData(report);

        Assert.assertEquals(0, report.getData().size());
    }
}
