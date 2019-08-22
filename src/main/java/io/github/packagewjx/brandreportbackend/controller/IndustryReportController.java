package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.IndustryReport;
import io.github.packagewjx.brandreportbackend.service.IndustryReportService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-21
 **/
@Api(tags = "行业报告管理接口")
@RequestMapping("/industry-report")
@RestController
public class IndustryReportController extends BaseController<IndustryReport, String> {
    private IndustryReportService service;

    protected IndustryReportController(IndustryReportService service) {
        super(service, IndustryReport.class);
        this.service = service;
    }

    @Override
    @ApiOperation(value = "部分更新", hidden = true)
    public ResponseEntity<IndustryReport> partialUpdate(String s, IndustryReport updateVal) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ApiOperation(value = "构建行业报告")
    @ApiResponses({
            @ApiResponse(code = 200, message = "构建成功"),
    })
    @RequestMapping(value = "", method = RequestMethod.GET, params = {"build"})
    public ResponseEntity<IndustryReport>
    buildReport(@RequestParam @ApiParam(value = "行业名称") String industry,
                @RequestParam @ApiParam(value = "年份") Integer year,
                @RequestParam
                @ApiParam(value = "统计时长", allowableValues = "annual, monthly, quarterly") String period,
                @RequestParam(name = "period-time-number", required = false, defaultValue = "1")
                @ApiParam(name = "period-time-number", value = "年内统计时长序号", defaultValue = "1", allowableValues = "range[1, 12]") Integer periodTimeNumber,
                @RequestParam(required = false, defaultValue = "false") @ApiParam(value = "是否保存报告", defaultValue = "false", allowableValues = "true, false") boolean save) {
        IndustryReport report = service.buildIndustryReport(industry, year, period, periodTimeNumber);
        if (save) {
            report = service.save(report);
        }
        return new ResponseEntity<>(report, HttpStatus.OK);

    }
}
