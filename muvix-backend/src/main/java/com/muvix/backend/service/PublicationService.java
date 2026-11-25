package com.muvix.backend.service;

import com.muvix.backend.dto.request.*;
import com.muvix.backend.dto.response.PaginatedResponse;
import com.muvix.backend.dto.response.PublicationResponse;
import com.muvix.backend.exception.ConflictException;
import com.muvix.backend.exception.ResourceNotFoundException;
import com.muvix.backend.mapper.PublicationMapper;
import com.muvix.backend.model.*;
import com.muvix.backend.repository.ProductRepository;
import com.muvix.backend.repository.PublicationRepository;
import com.muvix.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicationService {
    private final PublicationRepository publications;
    private final ProductRepository products;
    private final UserRepository users;
    private final PublicationMapper mapper;

    public PublicationService(PublicationRepository publications,ProductRepository products,UserRepository users,PublicationMapper mapper){
        this.publications=publications;this.products=products;this.users=users;this.mapper=mapper;
    }

    @Transactional(readOnly=true)
    public PaginatedResponse<PublicationResponse> list(int page,int size){
        int safePage=Math.max(page,0), safeSize=Math.min(Math.max(size,1),100);
        Page<Publication> result=publications.findAllByState(Publication.State.AVAILABLE,PageRequest.of(safePage,safeSize));
        List<PublicationResponse> items=result.getContent().stream().map(mapper::toResponse).toList();
        return new PaginatedResponse<>(items,safePage,safeSize,result.getTotalElements(),result.getTotalPages());
    }
    @Transactional(readOnly=true) public PublicationResponse get(Long id,String email,boolean admin){
        Product product=products.findById(id).orElse(null);
        if(product!=null&&product.getState()==Product.State.STORED&&email!=null&&product.getOwner().getEmail().equalsIgnoreCase(email)) return asStoredItem(product);
        Publication p=publications.findById(id).orElseThrow(()->new ResourceNotFoundException("Publication not found"));
        if(p.getState()==Publication.State.SOFT_DELETED && !(admin||(email!=null&&p.getSeller().getEmail().equalsIgnoreCase(email)))) throw new ResourceNotFoundException("Publication not found");
        return mapper.toResponse(p);
    }
    @Transactional(readOnly=true) public List<PublicationResponse> mine(String email){
        return publications.findAllBySellerEmailIgnoreCaseAndStateNot(email,Publication.State.SOFT_DELETED).stream().map(mapper::toResponse).toList();
    }
    @Transactional(readOnly=true) public List<PublicationResponse> myStorage(String email){
        return products.findAllByOwnerEmailIgnoreCaseAndState(email,Product.State.STORED).stream().map(this::asStoredItem).toList();
    }
    @Transactional public PublicationResponse store(CreateStorageItemRequest request,String email){
        User owner=findUser(email);
        Product product=new Product();
        product.setOwner(owner);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setCondition(request.getCondition());
        product.setImages(request.getImages());
        Product saved=products.save(product);
        return asStoredItem(saved);
    }
    @Transactional public PublicationResponse createProduct(CreatePublicationRequest request,String email){
        User owner=findUser(email);
        Product product=new Product();
        product.setOwner(owner);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setCondition(request.getCondition());
        product.setImages(request.getImages());
        product.setState(Product.State.LISTED);
        product=products.save(product);
        Publication publication=new Publication();
        publication.setSeller(owner);
        publication.setType(Publication.Type.INDIVIDUAL);
        publication.setTitle(request.getTitle());
        publication.setDescription(request.getDescription());
        publication.setPrice(request.getPrice());
        publication.setCategory(request.getCategory());
        publication.setCondition(request.getCondition());
        publication.setImages(request.getImages());
        publication.setProductIds(List.of(product.getId()));
        publication.setBoxId(request.getBoxId());
        return mapper.toResponse(publications.save(publication));
    }
    @Transactional public PublicationResponse createBatch(CreateBatchRequest request,String email){
        User seller=findUser(email);
        List<Long> selected=request.getExistingProductIds();
        List<NewProductData> added=request.getProductsToCreate();
        if(selected.size()+added.size()<2) throw new ConflictException("A batch requires at least two products");
        if(new HashSet<>(selected).size()!=selected.size()) throw new ConflictException("Duplicate selected listing IDs are not allowed");
        List<Long> productIds=new ArrayList<>();
        for(Long publicationId:selected){
            Publication source=publications.findById(publicationId).orElseThrow(()->new ResourceNotFoundException("Selected product listing not found: "+publicationId));
            if(!source.getSeller().getId().equals(seller.getId())) throw new org.springframework.security.access.AccessDeniedException("Cannot add another seller's product to your batch");
            if(source.getType()!=Publication.Type.INDIVIDUAL || source.getState()!=Publication.State.AVAILABLE || source.getProductIds().size()!=1)
                throw new ConflictException("Only available individual listings can be added to a batch");
            Long productId=source.getProductIds().getFirst();
            Product existing=products.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found: "+productId));
            if(!existing.getOwner().getId().equals(seller.getId())) throw new ConflictException("Selected listing has an invalid product reference");
            productIds.add(productId);
        }
        for(NewProductData data:added){
            Product product=new Product();
            product.setOwner(seller);
            product.setTitle(data.getTitle());
            product.setDescription(data.getDescription());
            product.setCategory(data.getCategory());
            product.setCondition(data.getCondition());
            product.setImages(data.getImages());
            product.setState(Product.State.LISTED);
            productIds.add(products.save(product).getId());
        }
        Publication batch=new Publication();
        batch.setSeller(seller);
        batch.setType(Publication.Type.BATCH);
        batch.setTitle(request.getTitle());
        batch.setDescription(request.getDescription());
        batch.setPrice(request.getPrice());
        batch.setCategory(request.getCategory());
        batch.setCondition(null);
        batch.setImages(List.of());
        batch.setProductIds(productIds);
        return mapper.toResponse(publications.save(batch));
    }
    @Transactional public PublicationResponse update(Long id,ReplaceItemRequest request,String email,boolean admin){
        Publication publication=publications.findById(id).orElse(null);
        if(publication!=null){
            requireOwnerOrAdmin(publication.getSeller(),email,admin);
            if(new HashSet<>(request.getProductIds()).size()!=request.getProductIds().size()) throw new ConflictException("Duplicate product IDs are not allowed");
            if(publication.getType()==Publication.Type.INDIVIDUAL&&request.getProductIds().size()!=1) throw new ConflictException("An individual listing must contain exactly one product ID");
            for(Long productId:request.getProductIds()){
                Product product=products.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found: "+productId));
                requireOwnerOrAdmin(product.getOwner(),email,admin);
            }
            publication.setTitle(request.getTitle());
            publication.setDescription(request.getDescription());
            publication.setPrice(request.getPrice());
            publication.setCategory(request.getCategory());
            publication.setImages(request.getImages());
            publication.setProductIds(request.getProductIds());
            publication.setCondition(publication.getType()==Publication.Type.BATCH?null:request.getCondition());
            publication.setState(Publication.State.AVAILABLE);
            return mapper.toResponse(publications.save(publication));
        }
        Product product=products.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        requireOwnerOrAdmin(product.getOwner(),email,admin);
        if(request.getProductIds().size()!=1) throw new ConflictException("A single product listing must contain exactly one product ID");
        Product linked=products.findById(request.getProductIds().getFirst()).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        requireOwnerOrAdmin(linked.getOwner(),email,admin);
        Publication publicationFromStorage=new Publication();
        publicationFromStorage.setSeller(product.getOwner());
        publicationFromStorage.setType(Publication.Type.INDIVIDUAL);
        publicationFromStorage.setTitle(request.getTitle());
        publicationFromStorage.setDescription(request.getDescription());
        publicationFromStorage.setPrice(request.getPrice());
        publicationFromStorage.setCategory(request.getCategory());
        publicationFromStorage.setCondition(request.getCondition());
        publicationFromStorage.setImages(request.getImages());
        publicationFromStorage.setProductIds(request.getProductIds());
        product.setState(Product.State.LISTED);products.save(product);
        return mapper.toResponse(publications.save(publicationFromStorage));
    }
    private User findUser(String email){
        return users.findByEmailIgnoreCase(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
    private void requireOwnerOrAdmin(User owner,String email,boolean admin){
        if(!admin&&!owner.getEmail().equalsIgnoreCase(email))throw new org.springframework.security.access.AccessDeniedException("Not allowed to modify this resource");
    }
    private PublicationResponse asStoredItem(Product product){
        PublicationResponse response=new PublicationResponse();
        response.setId(product.getId());
        response.setType(Publication.Type.INDIVIDUAL);
        response.setSellerId(product.getOwner().getId());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setState(Publication.State.PAUSED);
        response.setCondition(product.getCondition());
        response.setImages(product.getImages());
        response.setProductIds(List.of(product.getId()));
        return response;
    }
}
