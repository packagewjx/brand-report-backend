package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.repository.data.CollectionRepository;
import io.github.packagewjx.brandreportbackend.service.CollectionService;
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
public class CollectionServiceImpl extends BaseServiceImpl<Collection, String> implements CollectionService {
    private CollectionRepository repository;

    @Autowired
    protected CollectionServiceImpl(CollectionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
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
