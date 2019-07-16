package io.github.packagewjx.brandreportbackend.repository;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
    List<Brand> findByBrandId(String brandid);

    List<Brand> findByBrandName(String name);

    List<Brand> findByIndustry(String industry);

    void deleteByBrandId(String brandid);

    void deleteByBrandName(String name);

    void deleteByIndustry(String industry);



}
