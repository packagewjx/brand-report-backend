package io.github.packagewjx.brandreportbackend;

import io.github.packagewjx.brandreportbackend.utils.UtilFunctions;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.*;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-20
 **/
public class UtilFunctionTest {
    @Test
    public void partialChange() {
        PartialTestClass tester = getOriginal();
        PartialTestClass original = getOriginal();

        // 测试1
        PartialTestClass newVal = new PartialTestClass();
        newVal.i = 10;
        UtilFunctions.partialChange(tester, newVal, null);
        Assert.assertEquals((Integer) 10, tester.i);
        Assert.assertTrue(UtilFunctions.deepEqualsWithExcludeFields(tester, original, Sets.newSet("i")));
        newVal.i = null;

        // 测试2
        tester = getOriginal();
        newVal.b = false;
        UtilFunctions.partialChange(tester, newVal, null);
        Assert.assertEquals(false, tester.b);
        Assert.assertTrue(UtilFunctions.deepEqualsWithExcludeFields(tester, original, Sets.newSet("b")));
        newVal.b = null;

        // 测试POJO
        tester = getOriginal();
        POJO pojo = new POJO();
        pojo.id = 2;
        pojo.val = 20;
        newVal.pojo = pojo;
        UtilFunctions.partialChange(tester, newVal, null);
        Assert.assertTrue(UtilFunctions.deepEqualsWithExcludeFields(tester, original, Sets.newSet("pojo")));
        Assert.assertEquals((Integer) 2, tester.pojo.id);
        Assert.assertEquals((Integer) 20, tester.pojo.val);
        newVal.pojo = null;

        // 测试Set
        tester = getOriginal();
        Set<POJO> set = new HashSet<>();
        set.add(pojo);
        newVal.set = set;
        UtilFunctions.partialChange(tester, newVal, null);
        Assert.assertTrue(UtilFunctions.deepEqualsWithExcludeFields(tester, original, Sets.newSet("set")));
        for (POJO next : tester.set) {
            if (next.id != 2) {
                continue;
            }
            Assert.assertEquals((Integer) 20, next.val);
        }
        newVal.set = null;

        // 测试Map
        tester = getOriginal();
        Map<String, String> map = new HashMap<>();
        map.put("1", "100");
        map.put("20", "20");
        newVal.map = map;
        UtilFunctions.partialChange(tester, newVal, null);
        Assert.assertTrue(UtilFunctions.deepEqualsWithExcludeFields(tester, original, Sets.newSet("map")));
        Assert.assertEquals("100", tester.map.get("1"));
        Assert.assertEquals("2", tester.map.get("2"));
        Assert.assertEquals("20", tester.map.get("20"));
        newVal.map = null;

        // 测试List
        tester = getOriginal();
        newVal.list = Lists.list("10", "20", "30", null, null, "100", "2000");
        UtilFunctions.partialChange(tester, newVal, null);
        Assert.assertTrue(UtilFunctions.deepEqualsWithExcludeFields(tester, original, Sets.newSet("list")));
        Assert.assertEquals("10", tester.list.get(0));
        Assert.assertEquals("20", tester.list.get(1));
        Assert.assertEquals("30", tester.list.get(2));
        Assert.assertEquals("4", tester.list.get(3));
        Assert.assertEquals("5", tester.list.get(4));
        Assert.assertEquals("100", tester.list.get(5));
        Assert.assertEquals("2000", tester.list.get(6));

        // 测试Array
        tester = getOriginal();
        String[] array = new String[6];
        array[0] = "10";
        array[5] = "100";
        newVal.array = array;
        UtilFunctions.partialChange(tester, newVal, null);
        Assert.assertEquals("10", tester.array[0]);
        Assert.assertEquals("100", tester.array[5]);
        Assert.assertEquals("2", tester.array[1]);
        newVal.array = null;
    }


    private PartialTestClass getOriginal() {
        // 原始数据
        PartialTestClass original = new PartialTestClass();
        original.i = 10;
        original.b = true;
        original.s = "hello world";
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        original.map = map;
        original.list = Lists.list("1", "2", "3", "4", "5");
        original.set = new HashSet<>();
        POJO pojo = new POJO();
        pojo.id = 1;
        pojo.val = 1;
        original.set.add(pojo);
        pojo = new POJO();
        pojo.id = 2;
        pojo.val = 2;
        original.set.add(pojo);
        pojo = new POJO();
        pojo.id = 3;
        pojo.val = 3;
        original.set.add(pojo);
        pojo = new POJO();
        pojo.id = 4;
        pojo.val = 4;
        original.set.add(pojo);
        pojo = new POJO();
        pojo.id = 5;
        pojo.val = 5;
        original.pojo = pojo;
        String[] array = new String[5];
        array[0] = "1";
        array[1] = "2";
        array[2] = "3";
        array[3] = "4";
        array[4] = "5";
        original.array = array;
        return original;
    }
}

class PartialTestClass {
    Integer i;
    Boolean b;
    String s;
    Map<String, String> map;
    List<String> list;
    String[] array;
    Set<POJO> set;
    POJO pojo;
}

class POJO {
    Integer id;

    Integer val;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        POJO pojo = (POJO) o;

        return id.equals(pojo.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}