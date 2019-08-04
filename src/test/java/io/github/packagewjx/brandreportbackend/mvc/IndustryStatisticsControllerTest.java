package io.github.packagewjx.brandreportbackend.mvc;

import io.github.packagewjx.brandreportbackend.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-4
 **/
public class IndustryStatisticsControllerTest extends BaseTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void count() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/industry-statistics?count&industry={in}&year={year}", "家电", 2018))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
