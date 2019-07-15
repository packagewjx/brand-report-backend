package io.github.packagewjx.brandreportbackend.repository.statistics;

import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IndustryStatisticsRepository extends MongoRepository<IndustryStatistics, String> {
    List<IndustryStatistics> findByStatId(String statid);

    List<IndustryStatistics> findByIndustryId(String industryid);

    List<IndustryStatistics> findByIndustryIdAndYear(String industryid, String year);

    List<IndustryStatistics> findByYear(Integer year);

    @Query(value = "{'year':{'$gt':?0,'$lt':?1}}")
    List<IndustryStatistics> findByYearBetween(Integer from, Integer to);

    void deleteByStatId(String statid);

    void deleteByIndustryId(String industryid);

    void deleteByIndustryIdAndYear(String industryid, Integer year);



}
