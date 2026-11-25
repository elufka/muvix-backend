package com.muvix.backend.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="CreateBatchRequest",description="Creates a batch publication from new products and existing individual listings.")
public class CreateBatchRequest {

    @Schema(example="Living room set")
    @NotBlank
    @Size(max=200)
    private String title;

    @Schema(example="Furniture to be sold together.")
    @NotBlank
    @Size(max=5000)
    private String description;

    @Schema(example="420.00",minimum="0")
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10,fraction=2)
    private BigDecimal price;

    @Schema(example="Furniture")
    @NotBlank
    @Size(max=100)
    private String category;

    @Schema(description="New products to create and include in this batch.")
    @NotNull
    @Valid
    private List<@Valid NewProductData> productsToCreate;

    @Schema(description="IDs of owned individual listings to include. These listings remain independently available.")
    @NotNull
    private List<@NotNull @Positive Long> existingProductIds;

}
