package com.muvix.backend.dto.request;

import com.muvix.backend.model.WishBox;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(name="CreateWishBoxRequest",description="Creates a sell, purchase, or full relocation box with source publication IDs.")
public class CreateWishBoxRequest {
    @NotNull
    @Schema(example="SELL")
    private WishBox.Type type;

    @Schema(example="2026-12-01",nullable=true)
    private LocalDate moveInDate;

    @Size(max=500)
    @Schema(example="Buenos Aires, Argentina",nullable=true)
    private String futureLocation;

    @Schema(example="2026-12-20",nullable=true)
    private LocalDate moveOutDate;

    @Size(max=30)
    private List<@NotNull @DecimalMin("0.0") @DecimalMax("100.0") @Digits(integer=3,fraction=2) BigDecimal> discountRule;

    @DecimalMin(value="0.0",inclusive=false)
    @Digits(integer=10,fraction=2)
    @Schema(example="1200.00",nullable=true)
    private BigDecimal takeItAllPrice;

    @NotNull
    @Schema(description="Publications to group in this box. A publication may be linked to multiple boxes.")
    private List<@NotNull @Positive Long> publicationIds;
}
