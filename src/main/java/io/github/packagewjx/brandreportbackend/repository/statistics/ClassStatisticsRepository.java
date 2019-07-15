package io.github.packagewjx.brandreportbackend.repository.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.ClassStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassStatisticsRepository extends MongoRepository<ClassStatistics, String> {
}
