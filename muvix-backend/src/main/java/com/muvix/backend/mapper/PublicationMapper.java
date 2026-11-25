package com.muvix.backend.mapper;
import com.muvix.backend.dto.request.CreatePublicationRequest;
import com.muvix.backend.dto.response.PublicationResponse;
import com.muvix.backend.model.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel="spring")
public interface PublicationMapper {
    @Mapping(target="id",ignore=true)
    @Mapping(target="type",ignore=true)
    @Mapping(target="seller",ignore=true)
    @Mapping(target="state",ignore=true)
    @Mapping(target="productIds",ignore=true)
    Publication toEntity(CreatePublicationRequest request);
    @Mapping(target="id",source="id")
    @Mapping(target="sellerId",source="seller.id")
    PublicationResponse toResponse(Publication publication);
}
