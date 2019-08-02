package io.github.packagewjx.brandreportbackend.repository;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BrandReportRepositoryTest extends BaseTest {

    @Autowired
    private BrandReportRepository brandReportRepository;

    /**
     * insert 在新增文档时，如果有一个相同的_ID时，就会新增失败
     */
    @Test
    public void testInsert() {
        BrandReport brandReport = new BrandReport();
        //第一条记录
        brandReport.setReportId("11"); //若使用mongodb自动生成的ID，就不用set Id了
        brandReport.setBrandId("meidi");
        brandReport.setYear(2018);
        brandReport.setPeriodTimeNumber(1);
        brandReport.setPeriod("quarter");
        brandReport.setCreateTime(new Date());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("hello", "world");
        dataMap.put("hello1", 123);
        dataMap.put("hello2", true);
        dataMap.put("hello3", color.RED);
        dataMap.put("hello4", color.values());//保存整个枚举列表
        brandReport.setData(dataMap);
        brandReportRepository.insert(brandReport);

        //第二条记录
        BrandReport brandReport1 = new BrandReport();
        brandReport1.setReportId("22");//若使用mongodb自动生成的ID，就不用set Id了
        brandReport1.setBrandId("haier");
        brandReport1.setYear(2018);
        brandReport1.setPeriodTimeNumber(1);
        brandReport1.setPeriod("quarter");
        brandReport1.setCreateTime(new Date());

        Map<String, Object> dataMap1 = new HashMap<>();
        dataMap1.put("hi", "bro");
        dataMap1.put("hi1", 123);
        dataMap1.put("hi2", true);
        dataMap1.put("hi3", color.RED);
        dataMap1.put("hi4", color.values());//保存整个枚举列表
        brandReport1.setData(dataMap1);

        brandReportRepository.insert(brandReport1);
    }

    /**
     * 在进行Save操作时，未指定新值的属性将会被置空，mongodb文档中原来的值也会被置空。
     * <p>
     * 所以，假设我们保存了一个有指定标题的BrandReport entity的实例：
     * 但后来我们发现我们想要更新某个属性。我们可以简单的从数据库从获取实体，进行更改并像之前一样使用save()方法。
     * <p>
     * 假设我们知道要修改的文档的id，我们可以使用CrudRepository的findById方法从数据库获取我们的实体：
     * <p>
     * 然后更改某个属性值，最后将更改save回数据库。
     */
    @Test
    public void testSave1() {
        BrandReport brandReport = new BrandReport();

        //修改id为1的year属性
        brandReport.setBrandId("meidi");
        brandReport.setYear(2013);          //改成2015
        brandReport.setPeriodTimeNumber(2);

        brandReportRepository.save(brandReport);
    }

    /**
     * Save()在新增文档时，如果有一个相同_ID的文档时，会覆盖原来的。
     * 未指定新值的属性将会被置空，mongodb文档中原来的值也会被置空
     */
    @Test
    public void testSave2() {
        BrandReport brandReport = new BrandReport();
        //修改id为1的year属性
        //brandReport.setReportId("333");
        brandReport.setBrandId("meidi");
        brandReport.setYear(2017);          //改成2017

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("hello", "world");
        brandReport.setData(dataMap);
        brandReportRepository.save(brandReport);
    }

    @Test
    public void testFindAll() {
        brandReportRepository.findAll().parallelStream().forEach(brandReport -> {
            //输出无序的喔...
            System.out.println(brandReport.getBrandId() + " ReportId data: " + brandReport.getReportId());
            System.out.println(brandReport.getBrandId() + " BrandId data: " + brandReport.getBrandId());
            System.out.println(brandReport.getBrandId() + "data: " + brandReport.getYear());
            System.out.println(brandReport.getBrandId() + "data: " + brandReport.getPeriodTimeNumber());
            System.out.println(brandReport.getBrandId() + "data: " + brandReport.getPeriod());
            System.out.println(brandReport.getBrandId() + "data: " + brandReport.getCreateTime());
            System.out.println(brandReport.getBrandId() + "data: " + brandReport.getData());
        });
    }

    @Test
    public void testFindOne() {
        BrandReport brandReport = new BrandReport();
        brandReport.setBrandId("meidi");
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        Example<BrandReport> example = Example.of(brandReport, exampleMatcher);

        if (brandReportRepository.findOne(example).isPresent()) {
            brandReport = brandReportRepository.findOne(example).get();
            System.out.println(brandReport.getBrandId() + " " + brandReport.getReportId() + " " + brandReport.getYear());
        }
    }

    @Test
    public void testFindById() {
        if (brandReportRepository.findById("22").isPresent()) {
            BrandReport brandReport = brandReportRepository.findById("22").get();
            System.out.println(brandReport.getBrandId() + " " + brandReport.getReportId() + " " + brandReport.getYear());
        }

    }

    @Test
    public void testDeleteById() {
        brandReportRepository.deleteById("22");
    }

    @Test
    public void testFindByYearGreaterThan() {
        brandReportRepository.findByYearGreaterThan(2015).parallelStream().forEach(brandReport -> System.out.println(brandReport.getReportId() + "  " + brandReport.getBrandId() + "  " + brandReport.getYear()));
    }

    @Test
    public void testFindByYearBetween() {
        brandReportRepository.findByYearBetween(2013, 2017).parallelStream().forEach(brandReport -> System.out.println(brandReport.getReportId() + "  " + brandReport.getBrandId() + "  " + brandReport.getYear()));
    }

    enum color {
        RED, GREEN, BLUE, YELLOW
    }

}
