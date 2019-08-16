package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.comment.BrandReportComment;
import io.github.packagewjx.brandreportbackend.service.BrandReportCommentService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
@RestController
@RequestMapping("brand-report-comment")
@Api(tags = "品牌报告评价访问接口")
public class BrandReportCommentController extends BaseController<BrandReportComment, String> {
    protected BrandReportCommentController(BrandReportCommentService service) {
        super(service);
    }

    @Override
    protected boolean isIdOfEntity(BrandReportComment entity, String s) {
        return Objects.equals(entity.getCommentId(), s);
    }
}
