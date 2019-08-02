package io.github.packagewjx.brandreportbackend.service.report;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.service.CollectionService;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本类将存储在Collection中的有效数据填充到BrandReport中。数据需要是与brandReport有相同的时间与品牌
 *
 * @author Junxian Wu
 */
@Service
public class CollectionDataImporter implements BrandReportDataImporter {
    private static final Logger logger = LoggerFactory.getLogger(CollectionDataImporter.class);
    private final CollectionService collectionService;

    public CollectionDataImporter(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Override
    public BrandReport importData(BrandReport brandReport) {
        if (brandReport == null) {
            logger.warn("品牌报告为空");
            return null;
        }
        logger.info("导入数据到品牌ID{}的品牌报告中", brandReport.getBrandId());
        if (brandReport.getData() == null) {
            logger.trace("原报告没有数据，新建数据中");
            brandReport.setData(new ConcurrentHashMap<>(64));
        }

        logger.debug("获取品牌ID{}在{}的数据", brandReport.getBrandId(), LogUtils.getLogTime(brandReport.getYear(), brandReport.getPeriod(), brandReport.getPeriodTimeNumber()));
        Collection clt = collectionService.getCombinedOneByTimeAndBrand(brandReport.getBrandId(), brandReport.getPeriod(),
                brandReport.getYear(), brandReport.getPeriodTimeNumber());
        logger.debug("导入数据到品牌ID{}的品牌报告中", brandReport.getBrandId());
        brandReport.getData().putAll(clt.getData());
        return brandReport;
    }
}
