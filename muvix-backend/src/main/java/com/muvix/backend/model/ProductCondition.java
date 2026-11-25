package com.muvix.backend.model;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description="Product condition. FAIR is also accepted for existing frontend forms.")
public enum ProductCondition { NEW, LIKE_NEW, VERY_GOOD, GOOD, USED, FAIR }
