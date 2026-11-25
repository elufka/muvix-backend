package com.muvix.backend.dto.response;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class WishBoxPublicationResponse {
    private PublicationResponse publication;
    private BigDecimal currentBoxPrice;
}
