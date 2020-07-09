package eu.ib.demoib.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class ExchangeApiResponseDto {
    String currencyFrom;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    LocalDate date;

    @JsonProperty("time_last_updated")
    Instant timeLastUpdated;

    Map<String, BigDecimal> rates;

}
