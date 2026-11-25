package com.muvix.backend.dto.request;
import com.muvix.backend.model.ProductCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="NewProductData")
public class NewProductData {
    @io.swagger.v3.oas.annotations.media.Schema(example="Oak coffee table")
    @NotBlank
    @Size(max=200)
    private String title;

    @io.swagger.v3.oas.annotations.media.Schema(example="A sturdy table with minor wear.")
    @NotBlank
    @Size(max=5000)
    private String description;

    @io.swagger.v3.oas.annotations.media.Schema(example="Furniture")
    @NotBlank
    @Size(max=100)
    private String category;

    @io.swagger.v3.oas.annotations.media.Schema(example="GOOD")
    @NotNull
    private ProductCondition condition;

    @io.swagger.v3.oas.annotations.media.Schema(description="Image URLs.")
    @NotNull
    private List<@NotBlank @Size(max=2048) String> images;

}
