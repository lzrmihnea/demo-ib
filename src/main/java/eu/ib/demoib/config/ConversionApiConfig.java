package eu.ib.demoib.config;

import com.google.common.collect.Lists;
import org.javatuples.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class ConversionApiConfig {

    public static final String CLIENT_EXCHANGE_RATE_API_LATEST_URL = "https://api.exchangeratesapi.io/latest?base=";
    public static final String CLIENT_EXCHANGE_RATE_API_V4_URL = "https://api.exchangerate-api.com/v4/latest/";

    @Bean
    public List<Pair<WebClient, String>> webClientsWithUrls() {
        return Lists.newArrayList(
                Pair.with(webClientLatest(), CLIENT_EXCHANGE_RATE_API_LATEST_URL),
                Pair.with(webClientV4(), CLIENT_EXCHANGE_RATE_API_V4_URL));
    }

    public WebClient webClientLatest() {
        return WebClient.builder().baseUrl(CLIENT_EXCHANGE_RATE_API_LATEST_URL).build();
    }

    public WebClient webClientV4() {
        return WebClient.builder().baseUrl(CLIENT_EXCHANGE_RATE_API_V4_URL).build();
    }
}
