package com.muvix.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(name="UpdateWishBoxRequest",description="Updates a box's type-specific relocation dates, location, and selling prices.")
public class UpdateWishBoxRequest {
    private LocalDate moveInDate;

    @Size(max=500)
    private String futureLocation;

    private LocalDate moveOutDate;

    @Size(max=30)
    private List<@NotNull @DecimalMin("0.0") @DecimalMax("100.0") @Digits(integer=3,fraction=2) BigDecimal> discountRule;

    @DecimalMin(value="0.0",inclusive=false)
    @Digits(integer=10,fraction=2)
    private BigDecimal takeItAllPrice;
}
