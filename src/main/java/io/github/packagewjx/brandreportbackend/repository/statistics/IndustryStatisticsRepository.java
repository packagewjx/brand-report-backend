package io.github.packagewjx.brandreportbackend.repository.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryStatisticsRepository extends MongoRepository<IndustryStatistics, String> {
    List<IndustryStatistics> findByStatId(String statid);

    List<IndustryStatistics> findByIndustry(String industry);

    List<IndustryStatistics> findByIndustryAndYear(String industry, Integer year);

    List<IndustryStatistics> findByYear(Integer year);

    @Query(value = "{'year':{'$gt':?0,'$lt':?1}}")
    List<IndustryStatistics> findByYearBetween(Integer from, Integer to);

    void deleteByStatId(String statid);

    void deleteByIndustry(String industry);

    void deleteByIndustryAndYear(String industry, Integer year);


}
