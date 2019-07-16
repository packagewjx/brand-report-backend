package io.github.packagewjx.brandreportbackend.repository.data;

import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends MongoRepository<Collection, String> {
    List<Collection> findByCollectionId(String collectionid);

    List<Collection> findByBrandId(String brandid);

    @Query(value = "{'collectionId':?0, 'brandId':?1}")
    List<Collection> findByCollectionIdAndBrandId(String collectionid, String brandid);

    List<Collection> findByYear(Integer year);

    @Query(value = "{'year':{'$gt':?0}}")
    List<Collection> findByYearGreaterThan(Integer gtYear);

    @Query(value = "{'year':{'$gt':?0, '$lt':?1}}")
    List<Collection> findByYearBetween(Integer from, Integer to);

    @Query(value = "{'collectionId':?0, 'year':?1}")
    List<Collection> findByCollectionIdAndYear(String collectionid, Integer year);

    @Query(value = "{'brandId':?0, 'year':?1}")
    List<Collection> findByBrandIdAndYear(String brandid, Integer year);

    void deleteByCollectionId(String collectionid);

    void deleteByBrandId(String brandid);

    void deleteByYear(Integer year);
}
