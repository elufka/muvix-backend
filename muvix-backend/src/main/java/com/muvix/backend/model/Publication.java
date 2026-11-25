package com.muvix.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name="publications")
@Getter @Setter @NoArgsConstructor
public class Publication {
    @Schema(description="INDIVIDUAL publications have one product ID; BATCH publications have multiple.")
    public enum Type { INDIVIDUAL, BATCH }
    @Schema(description="Publication availability in the catalog.")
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
    @Column(nullable=false,updatable=false)
    private Instant createdAt=Instant.now();

}
