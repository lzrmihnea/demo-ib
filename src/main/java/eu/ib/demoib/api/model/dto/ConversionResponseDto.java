package eu.ib.demoib.api.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class ConversionResponseDto {

    @NotBlank
    @Size(min = 3, max = 3)
    String from;

    @NotBlank
    @Size(min = 3, max = 3)
    String to;

    @NotNull
    @PositiveOrZero
    BigDecimal amount;

    @NotNull
    @PositiveOrZero
    BigDecimal converted;

    public ConversionResponseDto(@NotBlank @Size(min = 3, max = 3) String from, @NotBlank @Size(min = 3, max = 3) String to, @NotNull @PositiveOrZero BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
