package com.muvix.backend.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
public class CreateBatchRequest {

    @NotBlank
    @Size(max=200)
    private String title;

    @NotBlank
    @Size(max=5000)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10,fraction=2)
    private BigDecimal price;

    @NotBlank
    @Size(max=100)
    private String category;

    @NotNull
    @Valid
    private List<@Valid NewProductData> productsToCreate;

    @NotNull
    private List<@NotNull @Positive Long> existingProductIds;

    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public List<NewProductData> getProductsToCreate(){return productsToCreate;} public void setProductsToCreate(List<NewProductData> v){productsToCreate=v;}
    public List<Long> getExistingProductIds(){return existingProductIds;} public void setExistingProductIds(List<Long> v){existingProductIds=v;}
}
