package com.muvix.backend.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="RegisterRequest")
public class RegisterRequest {
    private String photo;
    @Schema(example="Ava",description="User's first name.")
    @NotBlank
    @Size(max=100)
    private String firstName;

    @Schema(example="Rivera",description="User's last name.")
    @NotBlank
    @Size(max=100)
    private String lastName;

    @Schema(example="ava@example.com")
    @NotBlank
    @Email
    @Size(max=254)
    private String email;

    @Schema(format="password",description="At least 8 characters.")
    @NotBlank
    @Size(min=8, max=100)
    private String password;

    @Schema(example="+1 555 0100")
    @NotBlank
    @Size(max=40)
    private String phone;

    @NotBlank
    @Size(max=100)
    private String country;

    @NotBlank
    @Size(max=100)
    private String province;

    @NotBlank
    @Size(max=100)
    private String city;

    @NotBlank
    @Size(max=255)
    private String address;

}
