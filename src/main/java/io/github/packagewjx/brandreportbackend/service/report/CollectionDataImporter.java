package io.github.packagewjx.brandreportbackend.service.report;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.service.impl.CollectionService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本类将存储在Collection中的有效数据填充到BrandReport中。数据需要是与brandReport有相同的时间与品牌
 *
 * @author Junxian Wu
 */
@Service
public class CollectionDataImporter implements BrandReportDataImporter {
    private final CollectionService collectionService;

    public CollectionDataImporter(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Override
    public BrandReport importData(BrandReport brandReport) {
        if (brandReport == null) {
            return null;
        }
        if (brandReport.getData() == null) {
            brandReport.setData(new ConcurrentHashMap<>(64));
        }
        Collection clt = collectionService.getCombinedOneByTimeAndBrand(brandReport.getBrandId(), brandReport.getPeriod(),
                brandReport.getYear(), brandReport.getMonth(), brandReport.getQuarter());
        brandReport.getData().putAll(clt.getData());
        return brandReport;
    }
}
