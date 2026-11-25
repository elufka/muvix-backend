package com.muvix.backend.dto.response;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="UserResponse")
public class UserResponse {
    @Schema(example="7") private Long id;
    private String photo, firstName, lastName, email, phone, country, province, city, address;
    @Schema(description="Empty for standard users; contains ADMIN for platform administrators.") private List<RoleResponse> roles;

}
