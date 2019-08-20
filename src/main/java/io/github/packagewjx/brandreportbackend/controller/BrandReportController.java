package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.service.BrandReportService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-3
 **/
@RestController
@RequestMapping("/brand-report")
@Api(tags = "品牌报告访问接口")
public class BrandReportController extends BaseController<BrandReport, String> {
    private BrandReportService service;

    public BrandReportController(BrandReportService service) {
        super(service, BrandReport.class);
        this.service = service;
    }

    @ApiOperation(value = "根据品牌ID与时间构建新报告", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "构建成功"),
            @ApiResponse(code = 400, message = "请求有误，可能是参数没提供或者参数错误")
    })
    @RequestMapping(value = "", params = {"build"}, method = RequestMethod.GET)
    public ResponseEntity<BrandReport> buildBrandReport
            (@RequestParam("brand-id") @ApiParam(name = "brand-id", value = "品牌ID", required = true) String brandId,
             @RequestParam @ApiParam(value = "年份", required = true) Integer year,
             @RequestParam(required = false, defaultValue = "annual")
             @ApiParam(value = "统计时长", defaultValue = "annual", allowableValues = "annual, monthly, quarterly") String period,
             @RequestParam(name = "period-name-number", required = false, defaultValue = "1")
             @ApiParam(name = "period-name-number", value = "年内统计时长序号", defaultValue = "1", allowableValues = "range[1,12]") Integer periodTimeNumber) {
        return new ResponseEntity<>(service.buildReport(brandId, year, period, periodTimeNumber), HttpStatus.OK);
    }

}
