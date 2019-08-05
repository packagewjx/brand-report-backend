package io.github.packagewjx.brandreportbackend.repository;

import io.github.packagewjx.brandreportbackend.domain.IndustryReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryReportRepository extends MongoRepository<IndustryReport, String> {
    List<IndustryReport> findByIndustryReportId(String industryReportId);

    List<IndustryReport> findByIndustry(String industry);

    List<IndustryReport> findByYear(Integer year);

    @Query("{'year':{'$gt':?0, '$lt':?1}}")
    List<IndustryReport> findByYearBetween(Integer from, Integer to);

    List<IndustryReport> findByPeriodTimeNumber(Integer periodTimeNumber);

    List<IndustryReport> findByPeriod(String period);

    void deleteByIndustryReportId(String industryReportId);

    void deleteByIndustry(String industry);

    void deleteByYear(Integer year);

    void deleteByPeriodTimeNumber(Integer periodTimeNumber);

    void deleteByPeriod(String period);
}
