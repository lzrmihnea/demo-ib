package eu.ib.demoib.client;

import com.google.common.collect.Lists;
import eu.ib.demoib.client.model.ExchangeApiResponseDto;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;

@Service
public class ExchangeRateApiFacade {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateApiFacade.class);

    private static final String CLIENT_EXCHANGE_RATE_API_LATEST_URL = "https://api.exchangeratesapi.io/latest?base=";
    private static final String CLIENT_EXCHANGE_RATE_API_V4_URL = "https://api.exchangerate-api.com/v4/latest/";
    private static final String WEB_CLIENT_FAULTY_URL = "https://www.reddit.com/.json";

    private final WebClient webClientFaulty = WebClient.builder().baseUrl(WEB_CLIENT_FAULTY_URL).build();
    private final WebClient webClientLatest = WebClient.builder().baseUrl(CLIENT_EXCHANGE_RATE_API_LATEST_URL).build();
    private final WebClient webClientV4 = WebClient.builder().baseUrl(CLIENT_EXCHANGE_RATE_API_V4_URL).build();
    private final List<Pair<WebClient, String>> webClients =
            Lists.newArrayList(
//                    Pair.with(webClientFaulty, WEB_CLIENT_FAULTY_URL),
                    Pair.with(webClientLatest, CLIENT_EXCHANGE_RATE_API_LATEST_URL),
                    Pair.with(webClientV4, CLIENT_EXCHANGE_RATE_API_V4_URL));

    public Mono<ExchangeApiResponseDto> exchangeFrom(String currency) {
        Mono<ExchangeApiResponseDto> resp = Mono.empty();
        return resp
                .switchIfEmpty(exchangeWithLogging(webClients.get(1), currency))
                .switchIfEmpty(exchangeWithLogging(webClients.get(0), currency));
    }

    public Mono<ExchangeApiResponseDto> exchangeWithLogging(Pair<WebClient, String> webClientPair, String currency) {
        WebClient webClient = webClientPair.getValue0();
        String webClientUrl = webClientPair.getValue1();
        return exchangeFrom(webClient, webClientUrl, currency);
    }

    private Mono<ExchangeApiResponseDto> exchangeFrom(WebClient webClient, String clientUrl, String currency) {
        return webClient.get()
                .uri("{currency}", currency)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> logErrorResponse(clientResponse, clientUrl))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> logErrorResponse(clientResponse, clientUrl))
                .bodyToMono(ExchangeApiResponseDto.class)
                .doOnError(throwable -> logErrorResponse(throwable, clientUrl));
    }

    private Mono<? extends Throwable> logErrorResponse(ClientResponse clientResponse, String clientUrl) {
        LOG.error("API {} response faulty: {}", clientResponse, clientUrl);
        return Mono.empty();
    }

    private Mono<? extends Throwable> logErrorResponse(Throwable exception, String clientUrl) {
        LOG.error("API {} response faulty: {}", exception, clientUrl);
        return Mono.empty();
    }
}
