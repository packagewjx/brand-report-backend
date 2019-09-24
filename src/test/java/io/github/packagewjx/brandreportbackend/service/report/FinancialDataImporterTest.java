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

public class FinancialDataImporterTest extends BaseTest {
    @Autowired
    FinancialDataImporter financialDataImporter;

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

        financialDataImporter.importData(report);

        Assert.assertNotEquals(0, report.getData().size());
    }
}
