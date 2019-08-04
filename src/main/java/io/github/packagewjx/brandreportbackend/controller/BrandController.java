package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-4
 **/
@RestController
@RequestMapping("brand")
public class BrandController extends BaseController<Brand, String> {
    protected BrandController(BaseService<Brand, String> service) {
        super(service);
    }

    @Override
    protected boolean isIdOfEntity(Brand entity, String s) {
        return Objects.equals(entity.getBrandId(), s);
    }


}