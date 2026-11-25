package com.muvix.backend.controller;

import com.muvix.backend.dto.request.CreateWishBoxRequest;
import com.muvix.backend.dto.request.UpdateWishBoxRequest;
import com.muvix.backend.dto.response.WishBoxResponse;
import com.muvix.backend.service.WishBoxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boxes")
@RequiredArgsConstructor
@Tag(name="Boxes", description="Organize publications for selling, purchasing, or a full relocation.")
@SecurityRequirement(name="bearerAuth")
public class WishBoxController {
    private final WishBoxService service;

    @PostMapping @Operation(summary="Create a box")
    public WishBoxResponse create(@Valid @RequestBody CreateWishBoxRequest request, Authentication auth) {
        return service.create(request, auth.getName());
    }

    @GetMapping("/me") @Operation(summary="List my boxes")
    public List<WishBoxResponse> mine(Authentication auth) { return service.listMine(auth.getName()); }

    @GetMapping("/catalog") @Operation(summary="Browse published sell and full relocation boxes")
    public List<WishBoxResponse> catalog() { return service.catalog(); }

    @GetMapping("/{id}") @Operation(summary="Get a box")
    public WishBoxResponse get(@PathVariable Long id, Authentication auth) { return service.getPublicOrOwned(id, auth == null ? null : auth.getName()); }

    @PutMapping("/{id}") @Operation(summary="Update box dates and pricing")
    public WishBoxResponse update(@PathVariable Long id, @Valid @RequestBody UpdateWishBoxRequest request, Authentication auth) {
        return service.update(id, request, auth.getName());
    }

    @PutMapping("/{id}/publications/{publicationId}") @Operation(summary="Add a publication to a box")
    public WishBoxResponse addPublication(@PathVariable Long id, @PathVariable Long publicationId, Authentication auth) {
        return service.addPublication(id, publicationId, auth.getName());
    }

    @DeleteMapping("/{id}/publications/{publicationId}") @Operation(summary="Remove a publication from a box")
    public WishBoxResponse removePublication(@PathVariable Long id, @PathVariable Long publicationId, Authentication auth) {
        return service.removePublication(id, publicationId, auth.getName());
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Operation(summary="Delete a box")
    public void delete(@PathVariable Long id, Authentication auth) { service.delete(id, auth.getName()); }
}
