package io.github.packagewjx.brandreportbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.BaseTest;
import io.github.packagewjx.brandreportbackend.domain.Brand;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.Constants;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-2
 **/
public class BrandReportServiceTest extends BaseTest {
    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandReportService brandReportService;

    @Test
    public void buildReport() {
        Iterable<Brand> all = brandService.getAll();
        all.forEach(brand -> {
            BrandReport report = brandReportService.buildReport(brand.getBrandId(), 2018, Constants.PERIOD_ANNUAL, null);
            Assert.assertNotNull(report.getData());
            Assert.assertNotEquals(0, report.getData().size());
        });
    }

    @Test
    public void buildReportById() {
        Optional<Brand> brand = brandService.getById("5d27faaf830b32608a73d054");
        System.out.println(brand.get().toString());
        if(brand.isPresent()){
            BrandReport report = brandReportService.buildReport(brand.get().getBrandId(), 2018, Constants.PERIOD_ANNUAL, null);
            Assert.assertNotNull(report.getData());
            Assert.assertNotEquals(0, report.getData().size());
            System.out.println(report.toString());
        }

    }

    @Test
    public void httpClientGetTest() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:5000/financial_data/5d27faaf830b32608a73d06a/2017")
                .get() // get post put 等
                .build();
        Call call = client.newCall(request);
        Response resp = null;
        String result = "";
        try {
            resp = call.execute();
            if (resp.body() != null) {
                result = resp.body().string();
            }
            ObjectMapper mapper = new ObjectMapper();
            Collection collection = mapper.readValue(result, Collection.class);
            System.out.println(collection.toString());
        } catch (IOException e) {
            System.out.println("General I/O exception: "+ e.getMessage());
            e.printStackTrace();
        } finally {
            if (resp != null) {
                resp.close(); // 别忘记关闭
            }
        }
    }
}
