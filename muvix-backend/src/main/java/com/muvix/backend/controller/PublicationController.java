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
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping({"/api/v1/publication","/api/v1/listings"})
@RequiredArgsConstructor
@Tag(name="Publications",description="Browse, create, manage, and group marketplace publications.")
public class PublicationController {
    private final PublicationService service;

    @GetMapping
    @Operation(summary="Browse available publications",description="Returns a page of currently available individual and batch publications.")
    @ApiResponses({@ApiResponse(responseCode="200",description="Publication page")})
    public PaginatedResponse<PublicationResponse> list(
            @Parameter(description="Zero-based page index",schema=@Schema(minimum="0")) @RequestParam(defaultValue="0") int page,
            @Parameter(description="Page size (1–100)",schema=@Schema(minimum="1",maximum="100")) @RequestParam(defaultValue="20") int size){
        return service.list(page,size);
    }
    @GetMapping("/me")
    @Operation(summary="List my publications",security=@SecurityRequirement(name="bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode="200",description="The authenticated user's publications",content=@Content(array=@ArraySchema(schema=@Schema(implementation=PublicationResponse.class)))),@ApiResponse(responseCode="401",description="Authentication required")})
    public List<PublicationResponse> mine(Authentication auth){
        return service.mine(auth.getName());
    }
    @GetMapping("/me/storage")
    @Operation(summary="List my unpublished products",security=@SecurityRequirement(name="bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode="200",description="The authenticated user's stored products",content=@Content(array=@ArraySchema(schema=@Schema(implementation=PublicationResponse.class)))),@ApiResponse(responseCode="401",description="Authentication required")})
    public List<PublicationResponse> myStorage(Authentication auth){
        return service.myStorage(auth.getName());
    }
    @GetMapping("/{id}")
    @Operation(summary="Get a publication or owned stored product")
    @ApiResponses({@ApiResponse(responseCode="200",description="Publication details",content=@Content(schema=@Schema(implementation=PublicationResponse.class))),@ApiResponse(responseCode="404",description="Publication not found")})
    public PublicationResponse get(@Parameter(description="Numeric publication or product ID",example="42") @PathVariable Long id,
                                   Authentication auth){
        return service.get(id,auth==null?null:auth.getName(),isAdmin(auth));
    }
    @PostMapping("/storage")
    @Operation(summary="Create a stored product",description="Creates a private product in the authenticated user's storage.",security=@SecurityRequirement(name="bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode="200",description="Stored product created",content=@Content(schema=@Schema(implementation=PublicationResponse.class))),@ApiResponse(responseCode="400",description="Invalid product data"),@ApiResponse(responseCode="401",description="Authentication required")})
    public PublicationResponse store(@Valid @RequestBody CreateStorageItemRequest request,
                                     Authentication auth){
        return service.store(request,auth.getName());
    }
    @PostMapping("/batch")
    @Operation(summary="Create a batch publication",description="Creates one batch publication from two or more new products and/or the seller's existing individual listings. Existing individual listings remain available.",security=@SecurityRequirement(name="bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode="200",description="Batch created",content=@Content(schema=@Schema(implementation=PublicationResponse.class))),@ApiResponse(responseCode="400",description="Invalid batch data"),@ApiResponse(responseCode="401",description="Authentication required"),@ApiResponse(responseCode="403",description="A selected listing belongs to another user"),@ApiResponse(responseCode="409",description="Batch has fewer than two products or contains an ineligible listing")})
    public PublicationResponse createBatch(@Valid @RequestBody CreateBatchRequest request,
                                           Authentication auth){
        return service.createBatch(request,auth.getName());
    }
    @PostMapping
    @Operation(summary="Create an individual publication",description="Creates a product and publishes it for sale.",security=@SecurityRequirement(name="bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode="200",description="Individual publication created",content=@Content(schema=@Schema(implementation=PublicationResponse.class))),@ApiResponse(responseCode="400",description="Invalid publication data"),@ApiResponse(responseCode="401",description="Authentication required")})
    public PublicationResponse createProduct(@Valid @RequestBody CreatePublicationRequest request,
                                             Authentication auth){
        return service.createProduct(request,auth.getName());
    }
    @PutMapping("/edit/{id}")
    @Operation(summary="Update or publish a product",description="Updates an owned publication or publishes a stored product. Administrators may update any seller's publication.",security=@SecurityRequirement(name="bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode="200",description="Publication saved",content=@Content(schema=@Schema(implementation=PublicationResponse.class))),@ApiResponse(responseCode="400",description="Invalid publication data"),@ApiResponse(responseCode="401",description="Authentication required"),@ApiResponse(responseCode="403",description="Resource is owned by another user"),@ApiResponse(responseCode="404",description="Product not found")})
    public PublicationResponse update(@Parameter(description="Numeric publication or stored product ID",example="42") @PathVariable Long id,
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
