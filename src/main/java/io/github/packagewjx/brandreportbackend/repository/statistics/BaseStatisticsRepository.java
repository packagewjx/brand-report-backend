package io.github.packagewjx.brandreportbackend.repository.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.BaseStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BaseStatisticsRepository extends MongoRepository<BaseStatistics, String> {
}
