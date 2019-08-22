package io.github.packagewjx.brandreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.exception.EntityNotExistException;
import io.github.packagewjx.brandreportbackend.exception.MethodNotSupportException;
import io.github.packagewjx.brandreportbackend.service.BaseService;
import io.github.packagewjx.brandreportbackend.utils.UtilFunctions;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public abstract class BaseController<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger("BaseController");

    private BaseService<T, ID> service;
    private Class<T> tClass;
    @Autowired
    private ObjectMapper mapper;

    protected BaseController(BaseService<T, ID> service, Class<T> tClass) {
        this.service = service;
        this.tClass = tClass;
    }

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
        if (!service.isIdOfEntity(id, entity)) {
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
            @ApiImplicitParam(type = "path", value = "要删除的实体ID", name = "id", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<T> deleteById(@PathVariable("id") ID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "将传入的对象转变为查询条件，查询符合的所有实体对象", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 400, message = "请求不正确，可能由于example的格式错误，需要是经过URL编码的JSON格式")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, params = {"action=query"})
    @ApiParam(name = "action", allowableValues = "query", required = true, value = "查询API的操作参数")
    public ResponseEntity<List<T>> getAllByExample(@RequestParam(name = "example") @ApiParam(value = "查询的条件，是实体对象的JSON表示", required = true) String example) {
        T t;
        try {
            t = mapper.readValue(example, tClass);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ArrayList<>(((Collection<T>) service.getAllByExample(t))), HttpStatus.OK);
    }

    /**
     * 对于Map，则替换相同键的部分。对于数组或List
     * 则替换下标相同的部分，且将超长的部分加入到数组中。对于Set，则替换HashCode相同的部分。对于基本类型及其包装类，则
     * 替换值。其他的类型，则递归进入替换
     *
     * @param id        目标的Id
     * @param updateVal 值
     * @return 更新后的值
     */
    @ApiOperation(value = "部分更新，只更新提交的实体对象中不是Null的部分")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功"),
            @ApiResponse(code = 400, message = "参数有误。请查看是否错误的修改了ID"),
            @ApiResponse(code = 404, message = "未找到实体。可能ID不正确"),
    })
    public ResponseEntity<T> partialUpdate(@PathVariable("id") ID id, @RequestBody T updateVal) {
        Optional<T> byId = service.getById(id);
        T entity = byId.orElseThrow(() -> new EntityNotExistException("没有ID为" + id + "的实体"));
        UtilFunctions.partialChange(entity, updateVal, logger);
        // 检查ID是否被更改了
        if (!service.isIdOfEntity(id, entity)) {
            throw new IllegalArgumentException("不允许修改实体ID");
        }
        entity = service.save(entity);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodNotSupportException.class})
    public ResponseEntity<Exception> illegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotExistException.class})
    public ResponseEntity<Exception> entityNotFound(EntityNotExistException e) {
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Exception> exception(Exception e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
