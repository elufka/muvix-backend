package com.muvix.backend.dto.response;

import com.muvix.backend.model.WishBox;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(name="WishBoxResponse")
public class WishBoxResponse {
    private Long id;
    private Long ownerId;
    private WishBox.Type type;
    private LocalDate moveInDate;
    private String futureLocation;
    private LocalDate moveOutDate;
    private List<BigDecimal> discountRule;
    private BigDecimal takeItAllPrice;
    private BigDecimal originalTotalPrice;
    private BigDecimal currentTotalPrice;
    private List<WishBoxPublicationResponse> publications;
}
