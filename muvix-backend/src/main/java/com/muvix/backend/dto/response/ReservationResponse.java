package com.muvix.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import com.muvix.backend.model.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(name="ReservationResponse")
public class ReservationResponse {
    private String id;
    private String publicationId;
    private Long userId;
    private BigDecimal price;
    @JsonProperty("pickup_date")
    private OffsetDateTime pickupDate;
    private String pin;
    private Reservation.Status status;
    private UserResponse user;
    private PublicationResponse publication;
}
