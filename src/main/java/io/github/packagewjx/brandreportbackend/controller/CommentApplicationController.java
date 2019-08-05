package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.comment.CommentApplication;
import io.github.packagewjx.brandreportbackend.service.CommentApplicationService;
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
@RequestMapping("comment-application")
@Api(tags = "专家评价申请访问接口")
public class CommentApplicationController extends BaseController<CommentApplication, String> {
    private CommentApplicationService service;

    protected CommentApplicationController(CommentApplicationService service) {
        super(service);
        this.service = service;
    }

    @Override
    protected boolean isIdOfEntity(CommentApplication entity, String s) {
        return Objects.equals(entity.getApplicantId(), s);
    }

    /**
     * 根据报告Id获取报告评价申请
     *
     * @param brandReportId 报告Id
     * @return 该报告的评价申请
     */
    @RequestMapping(value = "", params = {"brand-report-id"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据报告Id获取报告评价申请", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    public ResponseEntity<Collection<CommentApplication>> getByBrandReportId(
            @RequestParam(name = "brand-report-id") @ApiParam(name = "brand-report-id", required = true) String brandReportId) {
        return new ResponseEntity<>(service.getByBrandReportId(brandReportId), HttpStatus.OK);
    }

    /**
     * 根据申请人Id获取其申请的所有报告评价
     *
     * @param applicantId 申请人Id
     * @return 该申请人申请的报告
     */
    @RequestMapping(value = "", params = {"applicant-id"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据申请人Id获取其申请的所有报告评价", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    public ResponseEntity<Collection<CommentApplication>> geyByApplicantId(
            @RequestParam(name = "applicant-id") @ApiParam(name = "applicant-id", required = true) String applicantId) {
        return new ResponseEntity<>(service.getByApplicantId(applicantId), HttpStatus.OK);
    }

    /**
     * 根据专家用户ID获取其所有需要填写报告的申请
     *
     * @param expertId 专家用户ID
     * @return 所有申请
     */
    @RequestMapping
    @ApiOperation(value = "根据专家用户ID获取其所有需要填写报告的申请", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功")
    })
    public ResponseEntity<Collection<CommentApplication>> getByExpertId(
            @RequestParam(name = "expert-id") @ApiParam(name = "expert-id", required = true) String expertId) {
        return new ResponseEntity<>(service.getByExpertId(expertId), HttpStatus.OK);
    }
}
