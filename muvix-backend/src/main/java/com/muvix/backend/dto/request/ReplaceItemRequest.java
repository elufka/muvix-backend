package com.muvix.backend.dto.request;
import com.muvix.backend.model.ProductCondition;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="ReplaceItemRequest")
public class ReplaceItemRequest {
    @Schema(example="Wooden chair")
    @NotBlank
    @Size(max=200)
    private String title;

    @Schema(example="Solid wood, lightly used.")
    @NotBlank
    @Size(max=5000)
    private String description;

    @Schema(example="35.00",minimum="0")
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10,fraction=2)
    private BigDecimal price;

    @Schema(example="Furniture")
    @NotBlank
    @Size(max=100)
    private String category;

    @Schema(example="GOOD")
    @NotNull
    private ProductCondition condition;

    @Schema(description="Image URLs.")
    @NotNull
    private List<@NotBlank @Size(max=2048) String> images;

    @Schema(description="One ID for an individual publication; multiple IDs for a batch.")
    @NotNull
    private List<@Positive Long> productIds;

}
