package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.repository.statistics.IndustryStatisticsRepository;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.service.statistics.StatisticsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
@Service
public class IndustryStatisticsServiceImpl extends BaseServiceImpl<IndustryStatistics, String> implements IndustryStatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(IndustryStatisticsServiceImpl.class);
    private StatisticsCounter counter;

    protected IndustryStatisticsServiceImpl(IndustryStatisticsRepository repository, StatisticsCounter counter) {
        super(repository);
        this.counter = counter;
    }

    @Override
    public IndustryStatistics countStatistics(String industry, Integer year, String period, Integer periodTimeNumber) {
        if (year == null || industry == null) {
            throw new IllegalArgumentException("year和industry不能为空");
        }
        return counter.count(industry, year, period, periodTimeNumber);
    }

    @Override
    public String getId(IndustryStatistics entity) {
        return entity.getStatId();
    }
}
