package io.github.packagewjx.brandreportbackend;

import io.github.packagewjx.brandreportbackend.config.FinancialDataSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FinancialDataSettings.class})
public class BrandReportBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandReportBackendApplication.class, args);
    }

}
