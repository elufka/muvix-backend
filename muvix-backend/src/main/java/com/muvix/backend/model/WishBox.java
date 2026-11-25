package com.muvix.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="wish_boxes")
@Getter @Setter @NoArgsConstructor
public class WishBox {
    public enum Type { SELL, PURCHASE, FULL_RELOCATION }
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="wishBoxIdGenerator")
    @SequenceGenerator(name="wishBoxIdGenerator",sequenceName="muvix_resource_ids",allocationSize=1)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="owner_id",nullable=false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=30)
    private Type type;

    @Column(name="move_in_date")
    private LocalDate moveInDate;

    @Column(name="future_location",length=500)
    private String futureLocation;

    @Column(name="move_out_date")
    private LocalDate moveOutDate;

    @ElementCollection
    @CollectionTable(name="wish_box_discount_steps",joinColumns=@JoinColumn(name="wish_box_id"))
    @Column(name="discount_percent",nullable=false,precision=5,scale=2)
    @OrderColumn(name="step_order")
    private List<BigDecimal> discountRule=new ArrayList<>();

    @Column(name="take_it_all_price",precision=12,scale=2)
    private BigDecimal takeItAllPrice;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="wish_box_publications",
            joinColumns=@JoinColumn(name="wish_box_id"),
            inverseJoinColumns=@JoinColumn(name="publication_id"),
            uniqueConstraints=@UniqueConstraint(
                    name="uk_wish_box_publication",
                    columnNames={"wish_box_id","publication_id"}
            )
    )
    private Set<Publication> publications=new LinkedHashSet<>();

    @Column(nullable=false,updatable=false)
    private Instant createdAt=Instant.now();
}
