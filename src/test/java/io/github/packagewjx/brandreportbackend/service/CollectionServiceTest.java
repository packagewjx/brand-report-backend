package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.service.impl.CollectionService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
public class CollectionServiceTest extends BaseTest {
    @Autowired
    CollectionService service;

    @Test
    public void baseMethodTest() {
        Iterable<Collection> all = service.getAll();
        List<String> ids = new ArrayList<>();
        all.forEach(collection -> ids.add(collection.getCollectionId()));

        if (ids.size() == 0) {
            return;
        }
        System.out.println(ids.size());

        // 获取其中一个
        Optional<Collection> one = service.getById(ids.get(0));
        Assert.assertTrue(one.isPresent());
        Collection collection = one.get();
        Assert.assertEquals(ids.get(0), collection.getCollectionId());

        // 插入新的记录
        collection.setCollectionId(null);
        service.save(collection);
        Assert.assertNotNull(collection.getCollectionId());

        // 更新记录
        collection.getData().put("test", "test");
        service.save(collection);
        one = service.getById(collection.getCollectionId());
        Assert.assertTrue(one.isPresent());
        Assert.assertEquals("test", one.get().getData().get("test"));

        // 删除记录
        service.delete(collection);
    }
}
