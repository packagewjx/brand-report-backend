package io.github.packagewjx.brandreportbackend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.BaseService;
import io.github.packagewjx.brandreportbackend.service.BrandService;
import io.github.packagewjx.brandreportbackend.service.CollectionService;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-16
 **/
@Component
public class DatabaseInitializer {
    private static Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final IndexService indexService;
    private final CollectionService collectionService;
    private final BrandService brandService;
    private final ObjectMapper mapper;

    @Value("${indexFileName:#{null}}")
    private String indexFileName;
    @Value("${collectionFileName:#{null}}")
    private String collectionFileName;
    @Value("${brandFileName:#{null}}")
    private String brandFileName;
    @Value("${initializeDatabase:#{'false'}}")
    private String initializeDatabase;

    public DatabaseInitializer(IndexService indexService, CollectionService collectionService, BrandService brandService, ObjectMapper mapper) {
        this.indexService = indexService;
        this.collectionService = collectionService;
        this.brandService = brandService;
        this.mapper = mapper;
    }

    @PostConstruct
    public void initializeDatabase() throws IOException, NoSuchFieldException, IllegalAccessException {
        if ("true".equals(initializeDatabase)) {
            logger.info("准备初始化数据库");
            if (indexFileName != null) {
                logger.info("使用{}文件初始化Index集合", indexFileName);
                doInitialize(indexFileName, indexService, Index.class, "indexId");
            }
            if (collectionFileName != null) {
                logger.info("使用{}文件初始化Collection集合", collectionFileName);
                doInitialize(collectionFileName, collectionService, Collection.class, "collectionId");
            }
            if (brandFileName != null) {
                logger.info("使用{}文件初始化Brand集合", brandFileName);
                doInitialize(brandFileName, brandService, Brand.class, "brandId");
            }
            System.exit(0);
        }
    }

    private <T> void doInitialize(String fileName, BaseService<T, ?> service, Class<T> tClass, String idFieldName) throws IOException, IllegalAccessException, NoSuchFieldException {
        Scanner scanner = new Scanner(new File(fileName));
        Set<T> set = new HashSet<>();
        while (scanner.hasNextLine()) {
            String json = scanner.nextLine();
            JsonNode jsonNode = mapper.readTree(json);
            T t = mapper.readValue(json, tClass);
            Field idField = tClass.getDeclaredField(idFieldName);
            idField.setAccessible(true);
            idField.set(t, jsonNode.get("_id").asText());
            set.add(t);
        }
        logger.info("读取了{}条记录", set.size());
        service.saveAll(set);
        logger.info("保存成功");
        scanner.close();
    }
}
