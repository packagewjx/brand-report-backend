package io.github.packagewjx.brandreportbackend.mvc;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.meta.Index;
import io.github.packagewjx.brandreportbackend.service.IndexService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
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


}
