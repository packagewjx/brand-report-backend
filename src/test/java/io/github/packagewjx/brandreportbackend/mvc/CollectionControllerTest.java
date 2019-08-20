package io.github.packagewjx.brandreportbackend.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.service.CollectionService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-3
 **/
public class CollectionControllerTest extends BaseTest {
    @Autowired
    CollectionService service;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void GetAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/collection/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void partialUpdate() throws Exception {
        Iterable<Collection> all = service.getAll();
        Collection originalCollection = all.iterator().next();
        Collection update = new Collection();
        update.setYear(2019);
        update.setBrandId("wujunxianpai");
        // 取出其中一个key并更新
        String key = originalCollection.getData().keySet().iterator().next();
        update.setData(new HashMap<>());
        update.getData().put(key, "wujunxian");
        mockMvc.perform(MockMvcRequestBuilders.patch("/collection/" + originalCollection.getCollectionId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(update)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    Collection updatedCollection = mapper.readValue(json, Collection.class);
                    Assert.assertEquals((Integer) 2019, updatedCollection.getYear());
                    Assert.assertEquals("wujunxianpai", updatedCollection.getBrandId());
                    Assert.assertEquals("wujunxian", updatedCollection.getData().get(key));
                });
        // 改回原本的值
        service.save(originalCollection);
    }
}
