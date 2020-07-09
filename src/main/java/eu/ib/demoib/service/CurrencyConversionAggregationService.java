package eu.ib.demoib.service;

import eu.ib.demoib.api.model.dto.ConversionResponseDto;
import eu.ib.demoib.client.ExchangeRateApiFacade;
import eu.ib.demoib.client.model.ExchangeApiResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class CurrencyConversionAggregationService {

    private final ExchangeRateApiFacade exchangeRateApiFacade;

    public CurrencyConversionAggregationService(ExchangeRateApiFacade exchangeRateApiFacade) {
        this.exchangeRateApiFacade = exchangeRateApiFacade;
    }

    public ConversionResponseDto exchangeCurrency(String from, String to, BigDecimal fromAmount) {
        Mono<ExchangeApiResponseDto> exchangeApiResponseDtoMono = this.exchangeRateApiFacade.exchangeFrom(from);
        Mono<BigDecimal> convertedAmount = exchangeApiResponseDtoMono
                .map(consumer -> {
                    BigDecimal toRate = consumer.getRates().get(to);
                    return toRate.multiply(fromAmount);
                });

        return new ConversionResponseDto(from, to, fromAmount, convertedAmount.block());
    }
}
