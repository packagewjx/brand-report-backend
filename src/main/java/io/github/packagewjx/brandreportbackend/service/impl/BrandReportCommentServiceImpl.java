package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.comment.BrandReportComment;
import io.github.packagewjx.brandreportbackend.repository.comment.BrandReportCommentRepository;
import io.github.packagewjx.brandreportbackend.service.BrandReportCommentService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
@Service
public class BrandReportCommentServiceImpl extends BaseServiceImpl<BrandReportComment, String> implements BrandReportCommentService {
    private BrandReportCommentRepository repository;

    protected BrandReportCommentServiceImpl(BrandReportCommentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Collection<BrandReportComment> getByBrandReportId(String brandReportId) {
        return repository.findByBrandReportId(brandReportId);
    }

    @Override
    public Collection<BrandReportComment> getByUserId(String userId) {
        return repository.findByUserId(userId);
    }
}
