package eu.ib.demoib.service;

import eu.ib.demoib.api.model.dto.ConversionResponseDto;
import eu.ib.demoib.client.ExchangeRateApiFacade;
import eu.ib.demoib.client.model.ExchangeApiResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionAggregationServiceTest {

    private static final String EUR = "EUR";
    private static final String USD = "USD";
    private static final LocalDate EXCHANGE_DATE = LocalDate.of(2020, Month.JULY, 9);
    private static final Map<String, BigDecimal> RATES = getRates();

    @InjectMocks
    private CurrencyConversionAggregationService currencyConversionAggregationService;

    @Mock
    private ExchangeRateApiFacade exchangeRateApiFacade;

    @Test
    public void testCurrencyConversionApiReturnedCorrectRates_expectedCorrectConversionMade() {
        ExchangeApiResponseDto exchangeApiResp = new ExchangeApiResponseDto();
        exchangeApiResp.setCurrencyFrom(EUR);
        exchangeApiResp.setDate(EXCHANGE_DATE);
        exchangeApiResp.setRates(RATES);
        BigDecimal toConvert = BigDecimal.valueOf(3.14d);
        when(this.exchangeRateApiFacade.getConvertedAmount(EUR, USD)).thenReturn(exchangeApiResp);

        ConversionResponseDto conversionResp = currencyConversionAggregationService.exchangeCurrency(EUR, USD, BigDecimal.valueOf(3.14d));
        Assertions.assertEquals(EUR, conversionResp.getFrom());
        Assertions.assertEquals(USD, conversionResp.getTo());
        Assertions.assertEquals(toConvert, conversionResp.getAmount());
        Assertions.assertEquals(BigDecimal.valueOf(3.454d), conversionResp.getConverted());
    }

    @Test
    public void testCurrencyConversionApiReturnedEmptyRates_expectedConversionSetToNull() {
        ExchangeApiResponseDto exchangeApiResp = new ExchangeApiResponseDto();
        exchangeApiResp.setCurrencyFrom(EUR);
        exchangeApiResp.setDate(EXCHANGE_DATE);
        exchangeApiResp.setRates(new HashMap<>());
        BigDecimal toConvert = BigDecimal.valueOf(3.14d);
        when(this.exchangeRateApiFacade.getConvertedAmount(EUR, USD)).thenReturn(exchangeApiResp);

        ConversionResponseDto conversionResp = currencyConversionAggregationService.exchangeCurrency(EUR, USD, BigDecimal.valueOf(3.14d));
        Assertions.assertEquals(EUR, conversionResp.getFrom());
        Assertions.assertEquals(USD, conversionResp.getTo());
        Assertions.assertEquals(toConvert, conversionResp.getAmount());
        Assertions.assertNull(conversionResp.getConverted());
    }

    @Test
    public void testCurrencyConversionApiReturnedNull_expectedConversionSetToNull() {
        ExchangeApiResponseDto exchangeApiResp = new ExchangeApiResponseDto();
        exchangeApiResp.setCurrencyFrom(EUR);
        exchangeApiResp.setDate(EXCHANGE_DATE);
        exchangeApiResp.setRates(new HashMap<>());
        BigDecimal toConvert = BigDecimal.valueOf(3.14d);
        when(this.exchangeRateApiFacade.getConvertedAmount(EUR, USD)).thenReturn(exchangeApiResp);

        ConversionResponseDto conversionResp = currencyConversionAggregationService.exchangeCurrency(EUR, USD, BigDecimal.valueOf(3.14d));
        Assertions.assertEquals(EUR, conversionResp.getFrom());
        Assertions.assertEquals(USD, conversionResp.getTo());
        Assertions.assertEquals(toConvert, conversionResp.getAmount());
        Assertions.assertNull(conversionResp.getConverted());
    }

    private static Map<String, BigDecimal> getRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(USD, BigDecimal.valueOf(1.1d));
        return rates;
    }

}
