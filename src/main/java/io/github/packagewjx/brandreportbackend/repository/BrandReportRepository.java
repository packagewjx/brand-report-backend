package io.github.packagewjx.brandreportbackend.repository;

import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BrandReportRepository extends MongoRepository<BrandReport, String> {
    List<BrandReport> findByReportId(String reportid);

    List<BrandReport> findByBrandId(String brandid);

    List<BrandReport> findByYear(Integer year);

    @Query("{'year':{'$gt':?0}}")       //?0、?1、?2是函数参数的占位符，从左到右依次类推
    List<BrandReport> findByYearGreaterThan(Integer gtYear);

    @Query("{'year':{'$gt':?0, '$lt':?1}}")
    List<BrandReport> findByYearBetween(Integer from, Integer to);

    List<BrandReport> findByQuarter(Integer quarter);

    List<BrandReport> findByMonth(Integer month);

    List<BrandReport> findByPeriod(String period);

    List<BrandReport> findByCreateTime(Date createtime);

    /**
     * 主键是唯一的，每次只会删除一个
     * @param reportid 报告Id
     */
    void deleteByReportId(String reportid);

    /**
     * 不唯一的键，在delete的时候，会删除所有具有相同键的Documents
     * @param brandid 品牌Id
     */
    void deleteByBrandId(String brandid);

    void deleteByYear(Integer year);

    void deleteByQuarter(Integer quarter);

    void deleteByMonth(Integer month);

    void deleteByPeriod(String period);

    void deleteByCreateTime(Date createtime);
}
