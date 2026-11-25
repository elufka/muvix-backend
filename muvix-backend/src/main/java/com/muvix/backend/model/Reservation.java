package com.muvix.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="reservations")
@Getter @Setter @NoArgsConstructor
public class Reservation {
    public enum Status { ACTIVE, CANCELLED, COMPLETED }
    @Id
    @Column(length=36,updatable=false)
    private String id=UUID.randomUUID().toString();

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="buyer_id",nullable=false)
    private User buyer;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="publication_id",nullable=false)
    private Publication publication;

    @Column(nullable=false,precision=12,scale=2)
    private BigDecimal price;

    @Column(name="pickup_date")
    private OffsetDateTime pickupDate;

    @Column(length=5)
    private String pin;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    private Status status=Status.ACTIVE;

    @Column(nullable=false,updatable=false)
    private Instant createdAt=Instant.now();
}
