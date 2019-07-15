package io.github.packagewjx.brandreportbackend.repository.meta;

import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IndexRepository extends MongoRepository<Index, String> {
    List<Index> findByIndexId(String indexid);

    List<Index> findByDisplayName(String displayname);

    List<Index> findByParentIndexId(String parentIndexid);

    List<Index> findByType(String type);

    List<Index> findByPeriod(String period);

    void deleteByIndexId(String indexid);

}
