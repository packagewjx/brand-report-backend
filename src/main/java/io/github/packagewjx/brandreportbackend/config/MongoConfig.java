package io.github.packagewjx.brandreportbackend.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-15
 **/
@Configuration
public class MongoConfig implements InitializingBean {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    @Lazy
    private MappingMongoConverter mappingMongoConverter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 为了不自动加入_class到BSON中而修改设置
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
