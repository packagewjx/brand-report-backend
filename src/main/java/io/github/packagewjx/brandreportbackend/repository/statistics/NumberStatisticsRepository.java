package io.github.packagewjx.brandreportbackend.repository.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.NumberStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NumberStatisticsRepository extends MongoRepository<NumberStatistics, String> {
}
