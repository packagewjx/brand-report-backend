package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-4
 **/
@Api(tags = "行业统计数据接口")
@RequestMapping("/industry-statistics")
@RestController
public class IndustryStatisticsController extends BaseController<IndustryStatistics, String> {
    private IndustryStatisticsService industryStatisticsService;

    protected IndustryStatisticsController(IndustryStatisticsService service) {
        super(service, IndustryStatistics.class);
        this.industryStatisticsService = service;
    }

    @RequestMapping(value = "", params = "count", method = RequestMethod.GET)
    @ApiOperation(value = "统计行业数据。只执行统计，不保存数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "统计计算成功")
    })
    public ResponseEntity<IndustryStatistics> count(@RequestParam @ApiParam(value = "行业名", required = true) String industry,
                                                    @RequestParam @ApiParam(value = "统计年份", required = true) Integer year,
                                                    @RequestParam(defaultValue = "annual", required = false)
                                                    @ApiParam(value = "统计时长", defaultValue = "annual", allowableValues = "annual, monthly, quarterly") String period,
                                                    @RequestParam(name = "period-time-number", defaultValue = "1")
                                                    @ApiParam(name = "period-time-number", defaultValue = "1", allowableValues = "range[1, 12]") Integer periodTimeNumber) {
        return new ResponseEntity<>(industryStatisticsService.countStatistics(industry, year, period, periodTimeNumber), HttpStatus.OK);
    }
}
