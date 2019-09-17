package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.repository.BrandRepository;
import io.github.packagewjx.brandreportbackend.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
@Service
public class BrandServiceImpl extends BaseServiceImpl<Brand, String> implements BrandService {
    private BrandRepository brandRepository;

    protected BrandServiceImpl(BrandRepository repository) {
        super(repository);
        this.brandRepository = repository;
    }

    @Override
    public String getId(Brand entity) {
        return entity.getBrandId();
    }

    @Override
    public Collection<Brand> getByIndustry(String industry) {
        return brandRepository.findByIndustry(industry);
    }
}
