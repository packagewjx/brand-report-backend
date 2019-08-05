package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.comment.BrandReportComment;
import io.github.packagewjx.brandreportbackend.service.BrandReportCommentService;
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
 * @date 19-8-5
 **/
@RestController
@RequestMapping("brand-report-comment")
@Api(tags = "品牌报告评价访问接口")
public class BrandReportCommentController extends BaseController<BrandReportComment, String> {
    private BrandReportCommentService service;

    protected BrandReportCommentController(BrandReportCommentService service) {
        super(service);
        this.service = service;
    }

    @Override
    protected boolean isIdOfEntity(BrandReportComment entity, String s) {
        return Objects.equals(entity.getCommentId(), s);
    }

    /**
     * 根据品牌报告Id获取品牌报告的评价
     *
     * @param brandReportId 品牌报告Id
     * @return 品牌报告评价
     */
    @RequestMapping(value = "", params = {"brand-report-id"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据品牌报告Id获取品牌报告的评价", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    public ResponseEntity<Collection<BrandReportComment>> getByBrandReportId(
            @RequestParam(name = "brand-report-id") @ApiParam(name = "brand-report-id", required = true) String brandReportId) {
        return new ResponseEntity<>(service.getByBrandReportId(brandReportId), HttpStatus.OK);
    }

    /**
     * 根据编写者用户Id获取品牌报告评价
     *
     * @param userId 用户Id
     * @return 该用户的所有品牌报告评价
     */
    @RequestMapping(value = "", params = {"user-id"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据编写者用户Id获取品牌报告评价", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    public ResponseEntity<Collection<BrandReportComment>> getByUserId(
            @RequestParam(name = "user-id") @ApiParam(name = "user-id", required = true) String userId) {
        return new ResponseEntity<>(service.getByUserId(userId), HttpStatus.OK);
    }
}
