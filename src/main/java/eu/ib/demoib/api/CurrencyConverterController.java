package eu.ib.demoib.api;

import eu.ib.demoib.api.model.dto.ConversionRequestDto;
import eu.ib.demoib.api.model.dto.ConversionResponseDto;
import eu.ib.demoib.service.CurrencyConversionAggregationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency/convert")
public class CurrencyConverterController {

    private final CurrencyConversionAggregationService currencyConversionAggregationService;

    public CurrencyConverterController(CurrencyConversionAggregationService currencyConversionAggregationService) {
        this.currencyConversionAggregationService = currencyConversionAggregationService;
    }

    //    @Operation(summary = "Cancel a subscription based on a given autoRenewalId",
//            description = "Marks the associated subscription to not be renewed after the end of the current subscription period.")
//    @ApiResponse(responseCode = "204", description = "No Content, the request was received and has been successfully processed by ACP.")
//    @ApiResponse(responseCode = "400", description = "Cancellation failed, no active subscription found for given autoRenewalId", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema))
//    @ApiResponse(responseCode = "401", description = "Missing authorization.", content = @Content())
//    @ApiResponse(responseCode = "403", description = "User not allowed to access this endpoint / resource.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema))
//    @ApiResponse(responseCode = "404", description = "Cancellation failed, no subscription found for given autoRenewalId", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema))
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping
    public ConversionResponseDto convertCurrency() {
        return currencyConversionAggregationService.exchangeCurrency("EUR", "USD", null);
    }

    @PostMapping
    public ConversionResponseDto convertCurrencyFrom(@RequestBody  ConversionRequestDto request) {
        return currencyConversionAggregationService.exchangeCurrency(request.getFrom(), request.getTo(), request.getAmount());
    }

//    private void print(ExchangeApiResponseDto exchangeApiResponseDto) {
//        System.out.println(exchangeApiResponseDto);
//    }
}
