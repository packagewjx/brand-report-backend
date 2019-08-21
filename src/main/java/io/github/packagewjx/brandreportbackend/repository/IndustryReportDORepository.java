package io.github.packagewjx.brandreportbackend.repository;

import io.github.packagewjx.brandreportbackend.dataobject.IndustryReportDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-21
 **/
@Repository
public interface IndustryReportDORepository extends MongoRepository<IndustryReportDO, String> {

}
