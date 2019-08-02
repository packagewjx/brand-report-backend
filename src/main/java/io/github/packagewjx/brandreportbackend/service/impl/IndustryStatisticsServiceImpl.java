package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.repository.statistics.IndustryStatisticsRepository;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
@Service
public class IndustryStatisticsServiceImpl extends BaseServiceImpl<IndustryStatistics, String> implements IndustryStatisticsService {
    private IndustryStatisticsRepository repository;

    protected IndustryStatisticsServiceImpl(IndustryStatisticsRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<IndustryStatistics> getByIndustry(String industry) {
        return repository.findByIndustry(industry);
    }
}
