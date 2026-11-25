package com.muvix.backend.dto.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="AuthResponse")
public class AuthResponse {
    @Schema(description="Bearer access JWT.") private String accessToken;
    @Schema(description="Refresh JWT for obtaining a new access token.") private String refreshToken;
    @Schema(example="Bearer") private String tokenType="Bearer";
    @Schema(description="Access token lifetime in seconds.",example="86400") private long expiresInSeconds;

    public AuthResponse(String accessToken,String refreshToken,long expiresInSeconds){
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
        this.expiresInSeconds=expiresInSeconds;
    }

}
