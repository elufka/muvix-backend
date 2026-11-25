package com.muvix.backend.dto.response;
import com.muvix.backend.model.ProductCondition;
import com.muvix.backend.model.Publication;
import java.math.BigDecimal;
import java.util.List;
public class PublicationResponse {
    private Long id;
    private Publication.Type type;
    private Long sellerId;
    private String title,description,category;
    private BigDecimal price;
    private Publication.State state;
    private ProductCondition condition;
    private List<String> images;
    private List<Long> productIds;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Publication.Type getType(){return type;} public void setType(Publication.Type v){type=v;}
    public Long getSellerId(){return sellerId;} public void setSellerId(Long v){sellerId=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public Publication.State getState(){return state;} public void setState(Publication.State v){state=v;}
    public ProductCondition getCondition(){return condition;} public void setCondition(ProductCondition v){condition=v;}
    public List<String> getImages(){return images;} public void setImages(List<String> v){images=v;}
    public List<Long> getProductIds(){return productIds;} public void setProductIds(List<Long> v){productIds=v;}
}
