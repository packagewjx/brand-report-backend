package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.comment.CommentApplication;
import io.github.packagewjx.brandreportbackend.repository.comment.CommentApplicationRepository;
import io.github.packagewjx.brandreportbackend.service.CommentApplicationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-5
 **/
@Service
public class CommentApplicationServiceImpl extends BaseServiceImpl<CommentApplication, String> implements CommentApplicationService {
    private CommentApplicationRepository repository;

    protected CommentApplicationServiceImpl(CommentApplicationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Collection<CommentApplication> getByBrandReportId(String brandReportId) {
        return repository.findAllByBrandReportId(brandReportId);
    }

    @Override
    public Collection<CommentApplication> getByApplicantId(String applicantId) {
        return repository.findAllByApplicantId(applicantId);
    }

    @Override
    public Collection<CommentApplication> getByExpertId(String expertId) {
        List<CommentApplication> allApplication = repository.findAll();
        return allApplication.parallelStream()
                .filter(commentApplication -> commentApplication.getExpertUserIds().contains(expertId))
                .collect(Collectors.toSet());
    }
}
