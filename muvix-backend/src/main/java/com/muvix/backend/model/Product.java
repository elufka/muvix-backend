package com.muvix.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="products")
public class Product {

    public enum State { STORED, LISTED, SOLD }
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="productIdGenerator")
    @SequenceGenerator(name="productIdGenerator",sequenceName="muvix_resource_ids",allocationSize=1)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="owner_id",nullable=false)
    private User owner;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false,length=5000)
    private String description;

    @Column(nullable=false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ProductCondition condition;

    @ElementCollection
    @CollectionTable(name="product_images",joinColumns=@JoinColumn(name="product_id"))
    @Column(name="image_url",nullable=false,length=2048)
    @OrderColumn(name="image_order")
    private List<String> images=new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private State state=State.STORED;

    public Product(){}
    public Long getId(){return id;} public User getOwner(){return owner;} public void setOwner(User v){owner=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public ProductCondition getCondition(){return condition;} public void setCondition(ProductCondition v){condition=v;}
    public List<String> getImages(){return images;} public void setImages(List<String> v){images=v==null?new ArrayList<>():new ArrayList<>(v);}
    public State getState(){return state;} public void setState(State v){state=v;}
}
