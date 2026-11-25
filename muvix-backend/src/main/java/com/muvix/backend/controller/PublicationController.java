package com.muvix.backend.controller;
import com.muvix.backend.dto.request.*;
import com.muvix.backend.dto.response.PaginatedResponse;
import com.muvix.backend.dto.response.PublicationResponse;
import com.muvix.backend.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping({"/api/v1/publication","/api/v1/listings"})
public class PublicationController {
    private final PublicationService service;

    public PublicationController(PublicationService service){
        this.service=service;
    }

    @GetMapping
    public PaginatedResponse<PublicationResponse> list(@RequestParam(defaultValue="0") int page,
                                                       @RequestParam(defaultValue="20") int size){
        return service.list(page,size);
    }
    @GetMapping("/me")
    public List<PublicationResponse> mine(Authentication auth){
        return service.mine(auth.getName());
    }
    @GetMapping("/me/storage")
    public List<PublicationResponse> myStorage(Authentication auth){
        return service.myStorage(auth.getName());
    }
    @GetMapping("/{id}")
    public PublicationResponse get(@PathVariable Long id,
                                   Authentication auth){
        return service.get(id,auth==null?null:auth.getName(),isAdmin(auth));
    }
    @PostMapping("/storage")
    public PublicationResponse store(@Valid @RequestBody CreateStorageItemRequest request,
                                     Authentication auth){
        return service.store(request,auth.getName());
    }
    @PostMapping("/batch")
    public PublicationResponse createBatch(@Valid @RequestBody CreateBatchRequest request,
                                           Authentication auth){
        return service.createBatch(request,auth.getName());
    }
    @PostMapping
    public PublicationResponse createProduct(@Valid @RequestBody CreatePublicationRequest request,
                                             Authentication auth){
        return service.createProduct(request,auth.getName());
    }
    @PutMapping("/edit/{id}")
    public PublicationResponse update(@PathVariable Long id,
                                      @Valid @RequestBody ReplaceItemRequest request,
                                      Authentication auth){
        return service.update(id,request,auth.getName(),isAdmin(auth));
    }

    private boolean isAdmin(Authentication auth){
        return auth!=null&&auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
}
