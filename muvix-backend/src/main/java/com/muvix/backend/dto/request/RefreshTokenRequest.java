package com.muvix.backend.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
public class RefreshTokenRequest {
    @Schema(description="The refreshToken returned by register, login, or token refresh.")
    @NotBlank
    private String refreshToken;

}
