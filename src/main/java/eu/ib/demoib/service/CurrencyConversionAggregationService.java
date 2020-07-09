package eu.ib.demoib.service;

import eu.ib.demoib.api.model.dto.ConversionResponseDto;
import eu.ib.demoib.client.ExchangeRateApiFacade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConversionAggregationService {

    private final ExchangeRateApiFacade exchangeRateApiFacade;

    public CurrencyConversionAggregationService(ExchangeRateApiFacade exchangeRateApiFacade) {
        this.exchangeRateApiFacade = exchangeRateApiFacade;
    }

    public ConversionResponseDto exchangeCurrency(String from, String to, BigDecimal fromAmount) {
        ConversionResponseDto response = new ConversionResponseDto(from, to, fromAmount);
        this.exchangeRateApiFacade.exchangeFrom(from)
                .subscribe(consumer -> {
                    BigDecimal toRate = consumer.getRates().get(to);
                    response.setConverted(toRate.multiply(fromAmount));
                });
        return response;
    }
}
