package io.github.packagewjx.brandreportbackend.repository.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.BaseStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseStatisticsRepository extends MongoRepository<BaseStatistics, String> {
}
