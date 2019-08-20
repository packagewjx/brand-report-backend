package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.service.CollectionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-3
 **/
@RestController
@RequestMapping(value = "/collection")
@Api(tags = {"品牌报告数据集合访问接口"})
public class CollectionController extends BaseController<Collection, String> {
    protected CollectionController(CollectionService service) {
        super(service, Collection.class);
    }
}
