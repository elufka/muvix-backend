package com.muvix.backend.dto.request;
import com.muvix.backend.model.ProductCondition;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
public class CreatePublicationRequest {
    @NotBlank
    @Size(max=200)
    private String title;

    @NotBlank
    @Size(max=5000)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10, fraction=2)
    private BigDecimal price;

    @NotBlank
    @Size(max=100)
    private String category;

    @NotNull
    private ProductCondition condition;

    @NotNull
    private List<@NotBlank @Size(max=2048) String> images;

    @PositiveOrZero
    private Long boxId;

    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public ProductCondition getCondition(){return condition;} public void setCondition(ProductCondition v){condition=v;}
    public List<String> getImages(){return images;} public void setImages(List<String> v){images=v;}
    public Long getBoxId(){return boxId;} public void setBoxId(Long v){boxId=v;}
}
