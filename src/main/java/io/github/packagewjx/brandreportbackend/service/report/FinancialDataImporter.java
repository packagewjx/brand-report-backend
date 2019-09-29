package io.github.packagewjx.brandreportbackend.service.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.packagewjx.brandreportbackend.config.FinancialDataSettings;
import io.github.packagewjx.brandreportbackend.domain.BrandReport;
import io.github.packagewjx.brandreportbackend.domain.data.Collection;
import io.github.packagewjx.brandreportbackend.utils.LogUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinancialDataImporter implements BrandReportDataImporter {
    private static final Logger logger = LoggerFactory.getLogger(FinancialDataImporter.class);

    @Autowired
    FinancialDataSettings financialDataSettings;

    @Override
    public BrandReport importData(BrandReport brandReport) {
        if (brandReport == null) {
            logger.warn("品牌报告为空");
            return null;
        }
        logger.info("正在导入财务数据到品牌ID{}的品牌报告中...", brandReport.getBrandId());
        if (brandReport.getData() == null) {
            logger.trace("原报告没有数据，新建数据中");
            brandReport.setData(new ConcurrentHashMap<>(64));
        }

        logger.debug("获取品牌ID{}在{}的财务数据", brandReport.getBrandId(), LogUtils.getLogTime(brandReport.getYear(),
                brandReport.getPeriod(), brandReport.getPeriodTimeNumber()));

        //http请求财报数据
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://"+ financialDataSettings.getDataSourceName() +":5000/financial_data/"+ brandReport.getBrandId() +"/" + brandReport.getYear())
                .get() // get post put 等
                .build();
        Call call = client.newCall(request);
        Response resp = null;
        String result = "";
        Collection collection_result = new Collection();

        try {
            resp = call.execute();
            if (resp.body() != null) {
                result = resp.body().string();
            }
            ObjectMapper mapper = new ObjectMapper();
            collection_result = mapper.readValue(result, Collection.class);
        } catch (IOException e) {
            logger.error("General I/O exception: "+ e.getMessage());
            e.printStackTrace();
        } finally {
            if (resp != null) {
                resp.close(); // 别忘记关闭
            }
        }

        //删除字段值为null的字段，避免NPE
        collection_result.getData().entrySet().removeIf(it -> it.getValue() == null);

        logger.debug("导入财务数据到品牌ID{}的品牌报告中完成", brandReport.getBrandId());
        brandReport.getData().putAll(collection_result.getData());
        return brandReport;
    }
}
