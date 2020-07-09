package eu.ib.demoib.client.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class ExchangeRateDto {
    String currency;
    BigDecimal rate;
}
