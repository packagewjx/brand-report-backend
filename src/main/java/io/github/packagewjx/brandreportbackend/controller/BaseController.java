package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.exception.EntityNotExistException;
import io.github.packagewjx.brandreportbackend.service.BaseService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public abstract class BaseController<T, ID> {
    private BaseService<T, ID> service;

    protected BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    /**
     * 用于确认这个id是否是entity的ID
     *
     * @param entity 所检查的entity
     * @param id     ID值
     * @return true代表id是entity的ID，false则不是
     */
    protected abstract boolean isIdOfEntity(T entity, ID id);

    @ApiOperation(value = "插入新的实体", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 201, message = "创建成功")
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<T> insert(@RequestBody @ApiParam(required = true, value = "新的实体") T entity) {
        return new ResponseEntity<>(service.save(entity), HttpStatus.CREATED);
    }

    @ApiOperation(value = "更新实体属性", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "实体ID", paramType = "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<T> update(@RequestBody @ApiParam(value = "更新后的实体") T entity,
                                    @PathVariable("id") ID id) {
        if (!isIdOfEntity(entity, id)) {
            throw new IllegalArgumentException("ID错误");
        }
        return new ResponseEntity<>(service.save(entity), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取对应实体", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "实体ID", paramType = "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "未找到对应的实体")
    })
    public ResponseEntity<T> getById(@PathVariable("id") ID id) {
        Optional<T> entity = service.getById(id);
        return entity.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "获取所有实体", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<T>> getAll() {
        return new ResponseEntity<>(new ArrayList<>(((Collection<T>) service.getAll())), HttpStatus.OK);
    }

    @ApiOperation(value = "删除实体", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<T> delete(@RequestBody @ApiParam(value = "需要删除的实体，比较重要的是要有ID") T entity) {
        service.delete(entity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "根据ID删除实体", httpMethod = "DELETE")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(type = "path", value = "要删除的实体ID", name = "id")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<T> deleteById(@PathVariable("id") ID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> illegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotExistException.class})
    public ResponseEntity<String> entityNotFound(EntityNotExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
