package io.github.packagewjx.brandreportbackend.controller;

import io.github.packagewjx.brandreportbackend.domain.comment.CommentApplication;
import io.github.packagewjx.brandreportbackend.service.CommentApplicationService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
@RestController
@RequestMapping("comment-application")
@Api(tags = "专家评价申请访问接口")
public class CommentApplicationController extends BaseController<CommentApplication, String> {

    protected CommentApplicationController(CommentApplicationService service) {
        super(service, CommentApplication.class);
    }

    @Override
    protected boolean isIdOfEntity(CommentApplication entity, String s) {
        return Objects.equals(entity.getApplicantId(), s);
    }
}
