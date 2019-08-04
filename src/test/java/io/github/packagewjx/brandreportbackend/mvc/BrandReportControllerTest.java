package io.github.packagewjx.brandreportbackend.mvc;

import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-3
 **/
public class BrandReportControllerTest extends BaseTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BrandService brandService;

    @Test
    public void normalBuild() {
        Iterable<Brand> all = brandService.getAll();
        all.forEach(brand -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/brand-report?build&brand-id={brandId}&year={year}&period={period}",
                        brand.getBrandId(), 2018, "annual"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
