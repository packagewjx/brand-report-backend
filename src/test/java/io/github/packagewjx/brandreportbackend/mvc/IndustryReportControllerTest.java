package io.github.packagewjx.brandreportbackend.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.IndustryReport;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-22
 **/
public class IndustryReportControllerTest extends BaseTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void buildSaveGetDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(new URI("/industry-report?build=true&industry=家电&year=2018&period=annual&save=true")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(buildResult -> {
                    IndustryReport report = mapper.readValue(buildResult.getResponse().getContentAsByteArray(), IndustryReport.class);
                    Assert.assertEquals("家电", report.getIndustry());
                    Assert.assertEquals((Integer) 2018, report.getYear());
                    Assert.assertEquals(Constants.PERIOD_ANNUAL, report.getPeriod());
                    Assert.assertNotNull(report.getStat());
                    Assert.assertNotNull(report.getBrandReports());
                    Assert.assertNotNull(report.getIndustryReportId());

                    // 获取报告
                    mockMvc.perform(MockMvcRequestBuilders.get("/industry-report/" + report.getIndustryReportId()))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(getResult -> {
                                IndustryReport getReport = mapper.readValue(getResult.getResponse().getContentAsByteArray(), IndustryReport.class);
                                Assert.assertEquals("家电", getReport.getIndustry());
                                Assert.assertEquals((Integer) 2018, getReport.getYear());
                                Assert.assertEquals(Constants.PERIOD_ANNUAL, getReport.getPeriod());
                                Assert.assertNotNull(getReport.getStat());
                                Assert.assertNotNull(getReport.getBrandReports());
                                Assert.assertNotNull(getReport.getIndustryReportId());

                                // 删除报告
                                mockMvc.perform(MockMvcRequestBuilders.delete("/industry-report/" + getReport.getIndustryReportId()))
                                        .andExpect(MockMvcResultMatchers.status().isOk());
                            });
                });
    }
}
