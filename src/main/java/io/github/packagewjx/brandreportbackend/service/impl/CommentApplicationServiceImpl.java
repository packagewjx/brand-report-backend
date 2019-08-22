package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.comment.CommentApplication;
import io.github.packagewjx.brandreportbackend.repository.comment.CommentApplicationRepository;
import io.github.packagewjx.brandreportbackend.service.CommentApplicationService;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
@Service
public class CommentApplicationServiceImpl extends BaseServiceImpl<CommentApplication, String> implements CommentApplicationService {

    protected CommentApplicationServiceImpl(CommentApplicationRepository repository) {
        super(repository);
    }

    @Override
    public String getId(CommentApplication entity) {
        return entity.getApplicationId();
    }
}
