package com.muvix.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(name="CreateReservationRequest",description="Creates a reservation for one publication.")
public class CreateReservationRequest {
    @Schema(description="Numeric publication ID serialized as a string by the frontend.",example="42")
    @NotBlank
    private String publicationId;

    @Schema(description="Optional buyer-owned PURCHASE or FULL_RELOCATION box containing this publication.", example="12", nullable=true)
    private Long boxId;

    @Schema(description="Buyer ID; must match the authenticated user.",example="7")
    @NotNull
    @Positive
    private Long userId;

    @Schema(description="Reservation price supplied by the client.",example="35.00",minimum="0")
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=10,fraction=2)
    private BigDecimal price;

    @Schema(description="Pickup time with timezone offset, or null while coordination is pending.",example="2026-12-01T10:30:00-03:00",nullable=true)
    @JsonProperty("pickup_date")
    private OffsetDateTime pickupDate;

    @Schema(description="Optional pickup PIN. The reservation page displays it to the buyer.",example="4821",nullable=true)
    @Size(max=5)
    private String pin;
}
