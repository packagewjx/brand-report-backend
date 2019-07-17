package io.github.packagewjx.brandreportbackend;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.service.impl.BaseServiceImpl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
@Service
public class BrandService extends BaseServiceImpl<Brand, String> {
    protected BrandService(CrudRepository<Brand, String> repository) {
        super(repository);
    }
}
