package io.github.packagewjx.brandreportbackend.service;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.service.impl.CollectionService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
public class CollectionServiceTest extends BaseTest {
    @Autowired
    CollectionService service;

    @Test
    public void baseMultipleMethodTest() {
        // 测试getall
        Iterable<Collection> all = service.getAll();
        List<Collection> collections = new ArrayList<>(30);
        all.forEach(collections::add);

        Assert.assertNotEquals(0, collections.size());

        // 检查字段是否齐全
        Collection collection = collections.get(0);
        Assert.assertNotNull(collection.getData());
        Assert.assertNotNull(collection.getCollectionId());
        Assert.assertNotNull(collection.getBrandId());
        Assert.assertNotNull(collection.getYear());
        Assert.assertNotNull(collection.getPeriod());

        // 测试根据ID集合get
        int testSize = 10;
        List<String> _10ids = collections.stream().limit(testSize).map(Collection::getCollectionId).collect(Collectors.toList());
        all = service.getAllById(_10ids);
        Assert.assertEquals(testSize, ((java.util.Collection<Collection>) all).size());
        all.forEach(c -> {
            Assert.assertTrue(_10ids.contains(c.getCollectionId()));
        });

        String[] ids = new String[2];
        // 制造两个无用的进行操作
        collection.setCollectionId(null);
        service.save(collection);
        ids[0] = collection.getCollectionId();
        collection.setCollectionId(null);
        service.save(collection);
        ids[1] = collection.getCollectionId();

        Optional<Collection> oc1 = service.getById(ids[0]);
        Assert.assertTrue(oc1.isPresent());
        Collection c1 = oc1.get();
        Collection c2 = collection;
        collection = null;

        c1.getData().put("c1", "c1");
        c2.getData().put("c2", "c2");

        List<Collection> testCollection = new ArrayList<>();
        testCollection.add(c1);
        testCollection.add(c2);
        // 测试saveall
        Iterable<Collection> retCol = service.saveAll(testCollection);
        retCol.forEach(c -> {
            if (c.getCollectionId().equals(c1.getCollectionId())) {
                Assert.assertEquals("c1", c.getData().get("c1"));
            } else if (c.getCollectionId().equals(c2.getCollectionId())) {
                Assert.assertEquals("c2", c.getData().get("c2"));
            } else {
                Assert.fail("返回值不包含c1或c2");
            }
        });

        // 删除
        service.deleteAll(retCol);
        Assert.assertFalse(service.getById(ids[0]).isPresent());
        Assert.assertFalse(service.getById(ids[1]).isPresent());
    }

    @Test
    public void baseSingleMethodTest() {
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


    @Test
    public void getCombinedOneByTimeAndBrand() {
        String brandId = service.getAll().iterator().next().getBrandId();
        Collection combinedOneByTimeAndBrand = service.getCombinedOneByTimeAndBrand(brandId, Constants.PERIOD_ANNUAL, 2018, null, null);
        Assert.assertNotNull(combinedOneByTimeAndBrand);
        Assert.assertNotNull(combinedOneByTimeAndBrand.getData());
        Assert.assertEquals((Integer) 2018, combinedOneByTimeAndBrand.getYear());
        Assert.assertEquals(Constants.PERIOD_ANNUAL, combinedOneByTimeAndBrand.getPeriod());
        Assert.assertEquals(brandId, combinedOneByTimeAndBrand.getBrandId());

        String[] id = new String[2];

        // 保证有两个数据集合
        Collection c1 = new Collection();
        c1.setBrandId(brandId);
        c1.setPeriod(Constants.PERIOD_ANNUAL);
        c1.setYear(2020);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("test1", 1);
        c1.setData(map);
        service.save(c1);
        id[0] = c1.getCollectionId();
        // 第二个集合
        c1.getData().remove("test1");
        c1.getData().put("test2", 2);
        c1.setCollectionId(null);
        service.save(c1);
        id[1] = c1.getCollectionId();

        Collection c = service.getCombinedOneByTimeAndBrand(brandId, Constants.PERIOD_ANNUAL, 2020, null, null);
        Assert.assertEquals(2, c.getData().size());

        // 删除多余的两个集合
        service.deleteById(id[0]);
        service.deleteById(id[1]);
    }
}
