package com.muvix.backend.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="LoginRequest")
public class LoginRequest {
    @Schema(example="ava@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(format="password")
    @NotBlank
    private String password;

}
