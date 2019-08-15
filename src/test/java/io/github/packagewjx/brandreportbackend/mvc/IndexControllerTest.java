package io.github.packagewjx.brandreportbackend.mvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-4
 **/
public class IndexControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndexService indexService;

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
    public void getIndexByType() throws Exception {
        String[] types = {"number", "enum", "bool", "indices"};
        for (String type : types) {
            mockMvc.perform(MockMvcRequestBuilders.get("/index?type={type}", type))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
        }
    }

    @Test
    public void getByExample() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType indexListType = mapper.getTypeFactory().constructParametricType(List.class, Index.class);

        // 测试一级数据
        Index i = new Index();
        i.setPeriod(Constants.PERIOD_ANNUAL);
        String json = mapper.writeValueAsString(i);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/index?action=query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(mvcResult -> {
                    List<Index> value = mapper.readValue(mvcResult.getResponse().getContentAsString(), indexListType);
                    value.forEach(index -> {
                        Assert.assertEquals(Constants.PERIOD_ANNUAL, index.getPeriod());
                    });
                });
        i.setPeriod(null);

        // 测试二级数据
        i.setAnnotations(new HashMap<>());
        i.getAnnotations().put(ScoreAnnotations.ANNOTATION_KEY_TYPE, ScoreAnnotations.StepScoreCounter.ANNOTATION_VALUE_TYPE);
        json = mapper.writeValueAsString(i);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/index?action=query")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> {
                    List<Index> indices = mapper.readValue(mvcResult.getResponse().getContentAsString(), indexListType);
                    indices.forEach(index -> {
                        Assert.assertEquals(ScoreAnnotations.StepScoreCounter.ANNOTATION_VALUE_TYPE, index.getAnnotations().get(ScoreAnnotations.ANNOTATION_KEY_TYPE));
                    });
                });
    }
}
