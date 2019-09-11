package io.github.packagewjx.brandreportbackend.mvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.meta.EnumRange;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.domain.meta.NumericRange;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import io.github.packagewjx.brandreportbackend.service.report.score.ScoreAnnotations;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-4
 **/
public class IndexControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndexService indexService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void getLeafIndicesOfRoot() throws Exception {
        Iterable<Index> all = indexService.getAll();
        List<Index> roots = new ArrayList<>();
        for (Index next : all) {
            if (next.getParentIndexId() == null) {
                roots.add(next);
            }
        }

        roots.forEach(index -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/index?root={root}", index.getIndexId()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                        .andDo(MockMvcResultHandlers.print());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // root为空
        mockMvc.perform(MockMvcRequestBuilders.get("/index?root"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getByExample() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType indexListType = mapper.getTypeFactory().constructParametricType(List.class, Index.class);

        // 测试一级数据
        Index i = new Index();
        i.setPeriod(Constants.PERIOD_ANNUAL);
        String json = mapper.writeValueAsString(i);
        String encodeJson = URLEncoder.encode(json, "UTF-8");
        URI uri = URI.create("/index?action=query&example=" + encodeJson);
        mockMvc.perform(MockMvcRequestBuilders
                .get(uri))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(mvcResult -> {
                    List<Index> value = mapper.readValue(mvcResult.getResponse().getContentAsString(), indexListType);
                    Assert.assertNotEquals(0, value.size());
                    value.forEach(index -> {
                        Assert.assertEquals(Constants.PERIOD_ANNUAL, index.getPeriod());
                    });
                });
        i.setPeriod(null);

        // 测试二级数据
        i.setAnnotations(new HashMap<>());
        i.getAnnotations().put(ScoreAnnotations.ANNOTATION_KEY_TYPE, ScoreAnnotations.StepScoreCounter.ANNOTATION_VALUE_TYPE);
        json = mapper.writeValueAsString(i);
        encodeJson = URLEncoder.encode(json, "utf-8");
        uri = URI.create("/index?action=query&example=" + encodeJson);
        mockMvc.perform(MockMvcRequestBuilders
                .get(uri))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> {
                    List<Index> indices = mapper.readValue(mvcResult.getResponse().getContentAsString(), indexListType);
                    Assert.assertNotEquals(0, indices.size());
                    indices.forEach(index -> {
                        Assert.assertEquals(ScoreAnnotations.StepScoreCounter.ANNOTATION_VALUE_TYPE, index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE));
                    });
                });
    }

    @Test
    public void rangeTest() throws Exception {
        // 测试数字类型值域
        Index index = new Index();
        index.setType(Index.TYPE_NUMBER);
        index.setDisplayName("测试指标");
        index.setPeriod(Constants.PERIOD_DEFAULT);
        index.setRange(new NumericRange(0.0, 100.0, 1.0));
        mockMvc.perform(MockMvcRequestBuilders.post("/index")
                .content(mapper.writeValueAsBytes(index))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(mvcResult -> {
                    Index savedIndex = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Index.class);
                    Assert.assertNotNull(savedIndex.getIndexId());
                    Assert.assertTrue(savedIndex.getRange() instanceof NumericRange);
                    Assert.assertEquals(0.0, ((NumericRange) savedIndex.getRange()).getMin(), 0);
                    Assert.assertEquals(100.0, ((NumericRange) savedIndex.getRange()).getMax(), 0);
                    Assert.assertNotNull(((NumericRange) savedIndex.getRange()).getStep());
                    Assert.assertEquals(1.0, ((NumericRange) savedIndex.getRange()).getStep(), 0);
                    // 删除
                    mockMvc.perform(MockMvcRequestBuilders.delete("/index/" + savedIndex.getIndexId()))
                            .andExpect(MockMvcResultMatchers.status().isOk());
                });

        // 测试枚举类型值域
        Set<Object> valueSet = new HashSet<>();
        valueSet.add("1");
        valueSet.add("2");
        valueSet.add("3");
        index.setRange(new EnumRange(valueSet));
        mockMvc.perform(MockMvcRequestBuilders.post("/index")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(index)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(mvcResult -> {
                    Index savedIndex = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Index.class);
                    Assert.assertNotNull(savedIndex.getIndexId());
                    Assert.assertTrue(savedIndex.getRange() instanceof EnumRange);
                    Assert.assertNotNull(savedIndex.getRange());
                    Assert.assertEquals(3, ((EnumRange) savedIndex.getRange()).getAllowableValues().size());
                    Assert.assertTrue(((EnumRange) savedIndex.getRange()).getAllowableValues().containsAll(valueSet));

                    // 删除
                    mockMvc.perform(MockMvcRequestBuilders.delete("/index/" + savedIndex.getIndexId()))
                            .andExpect(MockMvcResultMatchers.status().isOk());
                });


    }
}
