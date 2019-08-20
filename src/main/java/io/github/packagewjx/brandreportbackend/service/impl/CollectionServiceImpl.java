package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.repository.data.CollectionRepository;
import io.github.packagewjx.brandreportbackend.service.CollectionService;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
@Service
public class CollectionServiceImpl extends BaseServiceImpl<Collection, String> implements CollectionService {
    private final static Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private CollectionRepository repository;

    @Autowired
    protected CollectionServiceImpl(CollectionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Collection getCombinedOneByTimeAndBrand(String brandId, String period, Integer year, Integer periodTimeNumber) {
        logger.info("获取品牌ID{}在{}的所有报告数据并汇总中", brandId, LogUtils.getLogTime(year, period, periodTimeNumber));

        logger.debug("正在获取品牌ID{}在{}的所有数据", brandId, LogUtils.getLogTime(year, period, periodTimeNumber));
        List<Collection> byBrandIdAndYear = repository.findByBrandIdAndYear(brandId, year);

        if (byBrandIdAndYear.size() == 1) {
            logger.debug("品牌Id{}在{}仅有一份数据存档", brandId, LogUtils.getLogTime(year, period, periodTimeNumber));
            return byBrandIdAndYear.get(0);
        }

        Collection combine = new Collection();
        combine.setYear(year);
        combine.setPeriod(period);
        combine.setPeriodTimeNumber(periodTimeNumber);

        Stream<Collection> stream;
        if (period.equals(Constants.PERIOD_ANNUAL)) {
            stream = byBrandIdAndYear.stream();
        } else {
            if (periodTimeNumber == null) {
                logger.error("periodTimeNumber为空");
                throw new IllegalArgumentException("periodTimeNumber不能为空");
            }
            stream = byBrandIdAndYear.stream().filter(collection -> periodTimeNumber.equals(collection.getPeriodTimeNumber()));
        }

        logger.debug("组合品牌ID{}在{}的所有数据中", brandId, LogUtils.getLogTime(year, period, periodTimeNumber));
        Optional<Map<String, Object>> combinedMap = stream.map(Collection::getData)
                .reduce((stringObjectMap, stringObjectMap2) -> {
                    stringObjectMap.putAll(stringObjectMap2);
                    return stringObjectMap;
                });
        combine.setData(combinedMap.orElse(new HashMap<>(0)));
        return combine;
    }

    @Override
    public boolean isIdOfEntity(String s, Collection entity) {
        return Objects.equals(s, entity.getCollectionId());
    }
}
