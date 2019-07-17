package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.repository.statistics.IndustryStatisticsRepository;
import io.github.packagewjx.brandreportbackend.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
@Service
public class IndustryStatisticsService extends BaseServiceImpl<IndustryStatistics, String> {
    IndustryStatisticsRepository repository;

    protected IndustryStatisticsService(IndustryStatisticsRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Iterable<IndustryStatistics> getByIndustry(String industry) {
        return repository.findByIndustry(industry);
    }
}
