package eu.ib.demoib.service;

import eu.ib.demoib.api.model.dto.ConversionResponseDto;
import eu.ib.demoib.client.ExchangeRateApiFacade;
import eu.ib.demoib.client.model.ExchangeApiResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConversionAggregationService {

    private final ExchangeRateApiFacade exchangeRateApiFacade;

    public CurrencyConversionAggregationService(ExchangeRateApiFacade exchangeRateApiFacade) {
        this.exchangeRateApiFacade = exchangeRateApiFacade;
    }

    public ConversionResponseDto exchangeCurrency(String from, String to, BigDecimal fromAmount) {
        ExchangeApiResponseDto exchangeRateDto = this.exchangeRateApiFacade.getConvertedAmount(from, to);
        BigDecimal convertedAmount = getConvertedAmount(to, fromAmount, exchangeRateDto);
        return new ConversionResponseDto(from, to, fromAmount, convertedAmount);
    }

    private BigDecimal getConvertedAmount(String to, BigDecimal fromAmount, ExchangeApiResponseDto consumer) {
        if (consumer == null || consumer.getRates() == null) {
            return null;
        }
        BigDecimal toRate = consumer.getRates().get(to);
        if (toRate == null) {
            return null;
        }
        return fromAmount.multiply(toRate);
    }

}
