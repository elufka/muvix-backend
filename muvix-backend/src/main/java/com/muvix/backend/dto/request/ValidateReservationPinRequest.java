package com.muvix.backend.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class ValidateReservationPinRequest {
    @NotBlank
    @Size(max=5)
    @Schema(example="4821")
    private String pin;
}
