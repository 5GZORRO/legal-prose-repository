package eu._5gzorro.legalproserepository.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfig {
    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom().build();
    }
}
