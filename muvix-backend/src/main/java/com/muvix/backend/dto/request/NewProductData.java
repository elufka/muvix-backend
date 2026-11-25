package com.muvix.backend.dto.request;
import com.muvix.backend.model.ProductCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
public class NewProductData {
    @NotBlank
    @Size(max=200)
    private String title;

    @NotBlank
    @Size(max=5000)
    private String description;

    @NotBlank
    @Size(max=100)
    private String category;

    @NotNull
    private ProductCondition condition;

    @NotNull
    private List<@NotBlank @Size(max=2048) String> images;

    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public ProductCondition getCondition(){return condition;} public void setCondition(ProductCondition v){condition=v;}
    public List<String> getImages(){return images;} public void setImages(List<String> v){images=v;}
}
