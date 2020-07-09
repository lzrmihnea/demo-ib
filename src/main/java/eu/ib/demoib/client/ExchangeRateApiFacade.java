package eu.ib.demoib.client;

import com.google.common.collect.Lists;
import eu.ib.demoib.client.model.ExchangeApiResponseDto;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangeRateApiFacade {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateApiFacade.class);

    private static final String CLIENT_EXCHANGE_RATE_API_LATEST_URL = "https://api.exchangeratesapi.io/latest?base=";
    private static final String CLIENT_EXCHANGE_RATE_API_V4_URL = "https://api.exchangerate-api.com/v4/latest/";

    private final WebClient webClientLatest = WebClient.builder().baseUrl(CLIENT_EXCHANGE_RATE_API_LATEST_URL).build();
    private final WebClient webClientV4 = WebClient.builder().baseUrl(CLIENT_EXCHANGE_RATE_API_V4_URL).build();

    private final List<Pair<WebClient, String>> webClientWithUrls =
            Lists.newArrayList(
                    Pair.with(webClientLatest, CLIENT_EXCHANGE_RATE_API_LATEST_URL),
                    Pair.with(webClientV4, CLIENT_EXCHANGE_RATE_API_V4_URL));

    public ExchangeApiResponseDto getConvertedAmount(String from, String to, BigDecimal fromAmount) {
        for (Pair<WebClient, String> clientWithUrl : webClientWithUrls) {
            ExchangeApiResponseDto exchangeRates = getExchangeRates(clientWithUrl, from);
            if (isResponseValid(to, exchangeRates)) {
                return exchangeRates;
            }
        }
        LOG.error("All APIs are currently unavailable.");
        return null;
    }

    private boolean isResponseValid(String to, ExchangeApiResponseDto exchangeRates) {
        return exchangeRates != null
                && !CollectionUtils.isEmpty(exchangeRates.getRates())
                && exchangeRates.getRates().get(to) != null;
    }

    private ExchangeApiResponseDto getExchangeRates(Pair<WebClient, String> clientWithUrl, String from) {
        WebClient webClient = clientWithUrl.getValue0();
        String clientUrl = clientWithUrl.getValue1();

        return webClient.get()
                .uri("{currency}", from)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> logErrorResponse(clientResponse, clientUrl))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> logErrorResponse(clientResponse, clientUrl))
                .bodyToMono(ExchangeApiResponseDto.class)
                .block();
    }

    private Mono<? extends Throwable> logErrorResponse(ClientResponse clientResponse, String clientUrl) {
        LOG.error("API {} failed with HTTP status {}", clientUrl, clientResponse.statusCode());
        return Mono.empty();
    }
}
