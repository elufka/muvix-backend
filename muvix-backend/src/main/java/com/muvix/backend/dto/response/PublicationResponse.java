package com.muvix.backend.dto.response;
import com.muvix.backend.model.ProductCondition;
import com.muvix.backend.model.Publication;
import java.math.BigDecimal;
import java.util.List;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="PublicationResponse")
public class PublicationResponse {
    @Schema(description="Numeric publication ID.",example="42")
    private Long id;

    @Schema(description="INDIVIDUAL or BATCH.")
    private Publication.Type type;

    @Schema(example="7")
    private Long sellerId;

    @Schema(description="Legacy seller ID field used by the reservation frontend.")
    private Long userId;

    private String title,description,category;

    private BigDecimal price;

    @Schema(description="AVAILABLE, PAUSED, RESERVED, SOLD, or SOFT_DELETED.")
    private Publication.State state;

    @Schema(description="Legacy state alias used by existing frontend views.")
    private String status;

    private ProductCondition condition;

    private List<String> images;

    @Schema(description="IDs of the products included in this publication.")
    private List<Long> productIds;

    private String coverImage;

    private Instant createdAt;

}
