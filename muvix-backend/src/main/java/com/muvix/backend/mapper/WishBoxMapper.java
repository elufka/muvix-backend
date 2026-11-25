package com.muvix.backend.mapper;
import com.muvix.backend.dto.request.CreateWishBoxRequest;
import com.muvix.backend.dto.response.WishBoxResponse;
import com.muvix.backend.model.WishBox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel="spring")
public interface WishBoxMapper {
    @Mapping(target="id",ignore=true)
    @Mapping(target="owner",ignore=true)
    @Mapping(target="publications",ignore=true)
    @Mapping(target="createdAt",ignore=true)
    WishBox toEntity(CreateWishBoxRequest request);
    @Mapping(target="ownerId",source="owner.id")
    @Mapping(target="publications",ignore=true)
    @Mapping(target="originalTotalPrice",ignore=true)
    @Mapping(target="currentTotalPrice",ignore=true)
    WishBoxResponse toResponse(WishBox box);
}
