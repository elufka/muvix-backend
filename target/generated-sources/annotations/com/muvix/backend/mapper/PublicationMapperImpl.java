package com.muvix.backend.mapper;

import com.muvix.backend.dto.request.CreatePublicationRequest;
import com.muvix.backend.dto.response.PublicationResponse;
import com.muvix.backend.model.Publication;
import com.muvix.backend.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-25T14:03:28-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.13.0.2 (Microsoft)"
)
@Component
public class PublicationMapperImpl implements PublicationMapper {

    @Override
    public Publication toEntity(CreatePublicationRequest request) {
        if ( request == null ) {
            return null;
        }

        Publication publication = new Publication();

        publication.setTitle( request.getTitle() );
        publication.setDescription( request.getDescription() );
        publication.setPrice( request.getPrice() );
        publication.setCategory( request.getCategory() );
        publication.setCondition( request.getCondition() );
        List<String> list = request.getImages();
        if ( list != null ) {
            publication.setImages( new ArrayList<String>( list ) );
        }
        publication.setBoxId( request.getBoxId() );

        return publication;
    }

    @Override
    public PublicationResponse toResponse(Publication publication) {
        if ( publication == null ) {
            return null;
        }

        PublicationResponse publicationResponse = new PublicationResponse();

        publicationResponse.setId( publication.getId() );
        publicationResponse.setSellerId( publicationSellerId( publication ) );
        publicationResponse.setType( publication.getType() );
        publicationResponse.setTitle( publication.getTitle() );
        publicationResponse.setDescription( publication.getDescription() );
        publicationResponse.setCategory( publication.getCategory() );
        publicationResponse.setPrice( publication.getPrice() );
        publicationResponse.setState( publication.getState() );
        publicationResponse.setCondition( publication.getCondition() );
        List<String> list = publication.getImages();
        if ( list != null ) {
            publicationResponse.setImages( new ArrayList<String>( list ) );
        }
        List<Long> list1 = publication.getProductIds();
        if ( list1 != null ) {
            publicationResponse.setProductIds( new ArrayList<Long>( list1 ) );
        }

        return publicationResponse;
    }

    private Long publicationSellerId(Publication publication) {
        User seller = publication.getSeller();
        if ( seller == null ) {
            return null;
        }
        return seller.getId();
    }
}
