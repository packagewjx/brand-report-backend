package io.github.packagewjx.brandreportbackend.repository;

import io.github.packagewjx.brandreportbackend.domain.Brand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "classpath:spring-mongo.xml")
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testInsert() {
        Brand brand = new Brand();
        brand.setBrandId("3");
        brand.setBrandName("meidi");
        brand.setIndustry("家电");
        brandRepository.insert(brand);

        brand.setBrandId("4");
        brand.setBrandName("haier");
        brand.setIndustry("家电");
        brandRepository.insert(brand);
    }

    @Test
    public void testSave() {
        if(brandRepository.findById("1").isPresent()){
            Brand brand = brandRepository.findById("1").get();
            brand.setIndustry("改成了家电行业");
            brandRepository.save(brand);
        }
        else{
            System.out.println("not found");
        }

    }

    @Test
    public void testDelete() {
        brandRepository.deleteById("1");
    }

    @Test
    public void testFindByBrandname() {
        brandRepository.findByBrandName("haier").parallelStream().forEach(brand -> System.out.println(brand.getBrandName() + " id : " + brand.getBrandId()));
    }

    @Test
    public void testFindByIndustry() {
        brandRepository.findByIndustry("家电").parallelStream().forEach(brand -> System.out.println(brand.getBrandName() + " id : " + brand.getBrandId()));
    }

    @Test
    public void testDeleteByBrandId() {
        brandRepository.deleteByBrandId("2");

        brandRepository.findByIndustry("家电").parallelStream().forEach(brand -> System.out.println(brand.getBrandName() + " id : " + brand.getBrandId()));

    }
}
