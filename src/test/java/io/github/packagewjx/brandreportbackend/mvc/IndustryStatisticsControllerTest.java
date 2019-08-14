package io.github.packagewjx.brandreportbackend.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.statistics.IndustryStatistics;
import io.github.packagewjx.brandreportbackend.service.IndustryStatisticsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Autowired
    IndustryStatisticsService service;

    @Test
    public void count() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/industry-statistics?count&industry={in}&year={year}", "家电", 2018))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void save() throws Exception {
        IndustryStatistics statistics = service.countStatistics("家电", 2018, Constants.PERIOD_ANNUAL, 1);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(statistics);
        System.out.println(s);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/industry-statistics")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
