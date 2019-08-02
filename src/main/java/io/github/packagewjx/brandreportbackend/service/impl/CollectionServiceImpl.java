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
    public Collection getCombinedOneByTimeAndBrand(String brandId, String period, Integer year, Integer periodTimeNumber) {
        List<Collection> byBrandIdAndYear = repository.findByBrandIdAndYear(brandId, year);

        if (byBrandIdAndYear.size() == 1) {
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
                throw new IllegalArgumentException("periodTimeNumber不能为空");
            }
            stream = byBrandIdAndYear.stream().filter(collection -> periodTimeNumber.equals(collection.getPeriodTimeNumber()));
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
