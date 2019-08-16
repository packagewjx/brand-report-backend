package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Objects;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-4
 **/
@RestController
@RequestMapping("index")
@Api(tags = {"指标元数据访问接口"})
public class IndexController extends BaseController<Index, String> {
    private IndexService indexService;

    protected IndexController(IndexService service) {
        super(service, Index.class);
        indexService = service;
    }

    @Override
    protected boolean isIdOfEntity(Index entity, String s) {
        return Objects.equals(entity.getIndexId(), s);
    }

    @RequestMapping(value = "", params = {"root"}, method = RequestMethod.GET)
    @ApiOperation(value = "获取根ID为<root>的所有叶子指标对象，也就是包含具体数值的所有指标。" +
            "如果root为空字符串，或者null，则返回所有的叶子指标。", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "没有找到对应的根指标或其他未找到错误")
    })
    public ResponseEntity<Collection<Index>> getLeafIndicesOfRoot(@RequestParam(defaultValue = "") @ApiParam(value = "上级指标的ID", required = true) String root) {
        return new ResponseEntity<>(indexService.getLeafIndicesOfRoot(root), HttpStatus.OK);
    }

    @RequestMapping(value = "", params = "type", method = RequestMethod.GET)
    @ApiOperation(value = "获取类型为<type>的指标对象", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "没有对应的实体")
    })
    public ResponseEntity<Collection<Index>> getIndicesByType(@RequestParam @ApiParam(value = "指标类型", allowableValues = "number, string, enum, bool, indices") String type) {
        return new ResponseEntity<>(indexService.getAllByType(type), HttpStatus.OK);
    }
}
