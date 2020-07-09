package eu.ib.demoib.api;

import eu.ib.demoib.api.model.dto.ConversionRequestDto;
import eu.ib.demoib.api.model.dto.ConversionResponseDto;
import eu.ib.demoib.service.CurrencyConversionAggregationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency/convert")
public class CurrencyConverterController {

    private static final String HTTP_422_MESSAGE_REQ_INPUT_INVALID = "Request input invalid";
    private static final String HTTP_424_MESSAGE_DEPENDENCIES_UNAVAILABLE = "All APIs are currently unavailable";

    private final CurrencyConversionAggregationService currencyConversionAggregationService;

    public CurrencyConverterController(CurrencyConversionAggregationService currencyConversionAggregationService) {
        this.currencyConversionAggregationService = currencyConversionAggregationService;
    }

    @Operation(summary = "Converts a given amount from a currency to another")
    @ApiResponse(responseCode = "422", description = HTTP_422_MESSAGE_REQ_INPUT_INVALID, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "424", description = HTTP_424_MESSAGE_DEPENDENCIES_UNAVAILABLE, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @PostMapping
    public ResponseEntity convertCurrencyFrom(@RequestBody ConversionRequestDto request) {
        if (isRequestInvalid(request)) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(HTTP_422_MESSAGE_REQ_INPUT_INVALID);
        }
        ConversionResponseDto responseBody = currencyConversionAggregationService.exchangeCurrency(request.getFrom(), request.getTo(), request.getAmount());
        if (responseBody.getConverted() == null) {
            return ResponseEntity
                    .status(HttpStatus.FAILED_DEPENDENCY)
                    .body(HTTP_424_MESSAGE_DEPENDENCIES_UNAVAILABLE);
        }
        return ResponseEntity
                .ok(responseBody);
    }

    private boolean isRequestInvalid(@RequestBody ConversionRequestDto request) {
        return StringUtils.isBlank(request.getTo()) || StringUtils.isBlank(request.getFrom()) || request.getAmount() == null;
    }
}
