package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.repository.data.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
@Service
public class CollectionService extends BaseServiceImpl<Collection, String> {
    CollectionRepository repository;

    @Autowired
    protected CollectionService(CollectionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * 根据时间，获取数据，并合并为一个之后再返回。注意，返回的Collection没有ID
     *
     * @param brandId 品牌ID
     * @param year    年份
     * @param month   月份
     * @param quarter 季度
     * @param period  统计时长
     * @return 单个Collection，集合了所有指定品牌的指定统计时长的数据
     */
    public Collection getCombinedOneByTimeAndBrand(String brandId, String period, Integer year, Integer month, Integer quarter) {
        List<Collection> byBrandIdAndYear = repository.findByBrandIdAndYear(brandId, year);

        if (byBrandIdAndYear.size() == 1) {
            return byBrandIdAndYear.get(0);
        }

        Collection combine = new Collection();
        combine.setYear(year);
        combine.setMonth(month);
        combine.setQuarter(quarter);
        combine.setPeriod(period);

        Stream<Collection> stream;
        if (period.equals(Constants.PERIOD_MONTHLY)) {
            stream = byBrandIdAndYear.stream().filter(collection -> month.equals(collection.getMonth()));
        } else if (period.equals(Constants.PERIOD_QUARTERLY)) {
            stream = byBrandIdAndYear.stream().filter(collection -> quarter.equals(collection.getQuarter()));
        } else {
            stream = byBrandIdAndYear.stream();
        }

        Optional<Map<String, Object>> combinedMap = stream.map(Collection::getData)
                .reduce((stringObjectMap, stringObjectMap2) -> {
                    stringObjectMap.putAll(stringObjectMap2);
                    return stringObjectMap;
                });
        combine.setData(combinedMap.orElse(new HashMap<>(0)));
        return combine;
    }
}
