package io.github.packagewjx.brandreportbackend.repository.data;

import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath:spring-mongo.xml")
public class CollectionRepositoryTest {
    @Autowired
    CollectionRepository collectionRepository;

    @Test
    public void testInsert() {
        List<Collection> collections = new ArrayList<>();
        Collection collection1 = new Collection();
        collection1.setCollectionId("11");
        collection1.setBrandId("meidi");
        collection1.setYear(2018);
        collection1.setMonth(6);
        collection1.setQuarter(2);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("gross_output", 1000);
        map1.put("turnover", 8888);
        map1.put("profit", 3333);
        collection1.setData(map1);
        collections.add(collection1);

        Collection collection2 = new Collection();
        collection2.setCollectionId("22");
        collection2.setBrandId("meidi");
        collection2.setYear(2017);
        collection2.setMonth(6);
        collection2.setQuarter(2);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("gross_output", 1000);
        map2.put("turnover", 8888);
        map2.put("profit", 3333);
        collection2.setData(map2);
        collections.add(collection2);

        Collection collection3 = new Collection();
        collection3.setCollectionId("33");
        collection3.setBrandId("haier");
        collection3.setYear(2018);
        collection3.setMonth(6);
        collection3.setQuarter(2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("gross_output", 1000);
        map3.put("turnover", 8888);
        map3.put("profit", 3333);
        collection3.setData(map3);
        collections.add(collection3);
        collectionRepository.insert(collections);
    }

    @Test
    public void testFindByYearBetween() {
        if (!collectionRepository.findByYearBetween(2015, 2018).isEmpty()) {
            collectionRepository.findByYearBetween(2015, 2018).parallelStream().forEach(collection -> System.out.println(collection.getCollectionId()+ " "+collection.getBrandId()+" " +collection.getYear() ));
        }
    }

    @Test
    public void testFindByCollectionIdAndBrandId() {
        if(!collectionRepository.findByCollectionIdAndBrandId("33", "haier").isEmpty()){
            collectionRepository.findByCollectionIdAndBrandId("33", "haier").parallelStream().forEach(collection ->
                    System.out.println(collection.getCollectionId()+" "+ collection.getBrandId()+" "+collection.getYear()));
        }
    }

    @Test
    public void testFindCollectionIdAndYear() {
        if (!collectionRepository.findByCollectionIdAndYear("11", 2018).isEmpty()) {
            collectionRepository.findByCollectionIdAndYear("11", 2018).parallelStream().forEach(collection -> System.out.println(collection.getCollectionId()+ " "+collection.getBrandId()+" " +collection.getYear() ));
        }
    }

    @Test
    public void testFindBrandIdAndYear() {
        if (!collectionRepository.findByBrandIdAndYear("haier", 2018).isEmpty()) {
            collectionRepository.findByBrandIdAndYear("haier", 2018).parallelStream().forEach(collection -> System.out.println(collection.getCollectionId()+ " "+collection.getBrandId()+" " +collection.getYear() ));
        }
    }

    @Test
    public void testDeleteByBrandId() {
        collectionRepository.deleteByBrandId("meidi");
    }

}
