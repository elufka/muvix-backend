package com.muvix.backend.dto.request;
import com.muvix.backend.model.ProductCondition;
import jakarta.validation.constraints.*;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="CreateStorageItemRequest",description="Adds an unpublished product to the signed-in user's storage.")
public class CreateStorageItemRequest {
    @Schema(example="Dining table")
    @NotBlank
    @Size(max=200)
    private String title;

    @Schema(example="Stored for a future listing.")
    @NotBlank
    @Size(max=5000)
    private String description;

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

    @Schema(description="Optional WishBox ID.")
    @PositiveOrZero
    private Long boxId;

}
