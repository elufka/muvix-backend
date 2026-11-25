package com.muvix.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="publications")
public class Publication {
    public enum Type { INDIVIDUAL, BATCH }
    public enum State { AVAILABLE, PAUSED, RESERVED, SOLD, SOFT_DELETED }
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="publicationIdGenerator")
    @SequenceGenerator(name="publicationIdGenerator",sequenceName="muvix_resource_ids",allocationSize=1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    private Type type=Type.INDIVIDUAL;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="seller_id",nullable=false)
    private User seller;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false,length=5000)
    private String description;

    @Column(precision=12,scale=2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=30)
    private State state=State.AVAILABLE;

    @Column(nullable=false)
    private String category;

    @Enumerated(EnumType.STRING)
    private ProductCondition condition;

    @ElementCollection
    @CollectionTable(name="publication_images",joinColumns=@JoinColumn(name="publication_id"))
    @Column(name="image_url",nullable=false,length=2048)
    @OrderColumn(name="image_order")
    private List<String> images=new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="publication_product_ids",joinColumns=@JoinColumn(name="publication_id"))
    @Column(name="product_id",nullable=false)
    private List<Long> productIds=new ArrayList<>();

    @Column(name="box_id")
    private Long boxId;

    public Publication(){}
    public Long getId(){return id;} public Type getType(){return type;} public void setType(Type v){type=v;}
    public User getSeller(){return seller;} public void setSeller(User v){seller=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public State getState(){return state;} public void setState(State v){state=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public ProductCondition getCondition(){return condition;} public void setCondition(ProductCondition v){condition=v;}
    public List<String> getImages(){return images;} public void setImages(List<String> v){images=v==null?new ArrayList<>():new ArrayList<>(v);}
    public List<Long> getProductIds(){return productIds;} public void setProductIds(List<Long> v){productIds=v==null?new ArrayList<>():new ArrayList<>(v);}
    public Long getBoxId(){return boxId;} public void setBoxId(Long v){boxId=v;}
}
