package io.github.packagewjx.brandreportbackend.service.impl;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.repository.statistics.IndustryStatisticsRepository;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import io.github.packagewjx.brandreportbackend.service.statistics.StatisticsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
@Service
public class IndustryStatisticsServiceImpl extends BaseServiceImpl<IndustryStatistics, String> implements IndustryStatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(IndustryStatisticsServiceImpl.class);
    private IndustryStatisticsRepository repository;
    private StatisticsCounter counter;

    protected IndustryStatisticsServiceImpl(IndustryStatisticsRepository repository, StatisticsCounter counter) {
        super(repository);
        this.repository = repository;
        this.counter = counter;
    }

    @Override
    public List<IndustryStatistics> getByIndustry(String industry) {
        logger.info("获取行业{}的统计数据", industry);
        return repository.findByIndustry(industry);
    }

    @Override
    public IndustryStatistics countStatistics(String industry, Integer year, String period, Integer periodTimeNumber) {
        if (year == null || industry == null) {
            throw new IllegalArgumentException("year和industry不能为空");
        }
        return counter.count(industry, year, period, periodTimeNumber);
    }
}
