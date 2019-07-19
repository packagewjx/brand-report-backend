package io.github.packagewjx.brandreportbackend.service.report;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.BrandService;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreDataImporter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-18
 **/
public class ScoreDataImporterTest extends BaseTest {
    @Autowired
    CollectionDataImporter collectionDataImporter;

    @Autowired
    BrandService brandService;

    @Autowired
    ScoreDataImporter scoreDataImporter;

    @Test
    public void importScore() {
        BrandReport report = new BrandReport();
        report.setYear(2018);
        report.setPeriod(Constants.PERIOD_ANNUAL);
        report.setCreateTime(new Date());
        // 随机获取一个品牌
        Brand brand = brandService.getAll().iterator().next();
        report.setBrandId(brand.getBrandId());

        // 使用这个服务来填充原始数据
        collectionDataImporter.importData(report);
        Assert.assertNotNull(report.getData());

        scoreDataImporter.importData(report);
        Assert.assertNotNull(report.getData().get("value-score"));
        Assert.assertNotNull(report.getData().get("spread-score"));
        Assert.assertNotNull(report.getData().get("develop-score"));
        Assert.assertNotNull(report.getData().get("manage-score"));
        Assert.assertNotNull(report.getData().get("market-score"));
        Assert.assertNotNull(report.getData().get("relation-score"));
        Assert.assertNotNull(report.getData().get("channel-score"));
        Assert.assertNotNull(report.getData().get("product-score"));
    }
}
