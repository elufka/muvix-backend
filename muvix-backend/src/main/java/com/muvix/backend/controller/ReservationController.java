package com.muvix.backend.controller;

import com.muvix.backend.dto.request.CreateReservationRequest;
import com.muvix.backend.dto.request.ValidateReservationPinRequest;
import com.muvix.backend.dto.response.ReservationResponse;
import com.muvix.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name="Reservations",description="Reserve a publication, coordinate pickup, cancel reservations, and complete delivery with a PIN.")
@SecurityRequirement(name="bearerAuth")
public class ReservationController {
    private final ReservationService service;

    @PostMapping
    @Operation(summary="Create a reservation",description="Reserves an available publication immediately. The publication returns to AVAILABLE if cancelled.")
    @ApiResponses({@ApiResponse(responseCode="200",description="Reservation created",content=@Content(schema=@Schema(implementation=ReservationResponse.class))),@ApiResponse(responseCode="400",description="Invalid reservation data"),@ApiResponse(responseCode="401",description="Authentication required"),@ApiResponse(responseCode="403",description="Buyer ID does not match the authenticated user"),@ApiResponse(responseCode="404",description="Publication not found"),@ApiResponse(responseCode="409",description="Publication is not available")})
    public ReservationResponse create(@Valid @RequestBody CreateReservationRequest request,Authentication auth){
        return service.create(request,auth.getName());
    }

    @GetMapping
    @Operation(summary="List my active reservations",description="Returns active reservations where the current user is either buyer or seller.")
    @ApiResponses({@ApiResponse(responseCode="200",description="Active reservations"),@ApiResponse(responseCode="401",description="Authentication required")})
    public List<ReservationResponse> list(Authentication auth){return service.listVisible(auth.getName());}

    @GetMapping("/{id}")
    @Operation(summary="Get an active reservation")
    @ApiResponses({@ApiResponse(responseCode="200",description="Reservation details",content=@Content(schema=@Schema(implementation=ReservationResponse.class))),@ApiResponse(responseCode="404",description="Reservation not found or not visible to this user")})
    public ReservationResponse get(@PathVariable String id,Authentication auth){return service.getVisible(id,auth.getName());}

    @PostMapping("/{id}/validate-pin")
    @Operation(summary="Validate the pickup PIN",description="A correct seller-entered PIN marks the reservation complete and the publication SOLD. An incorrect PIN returns false.")
    @ApiResponses({@ApiResponse(responseCode="200",description="True when PIN is correct"),@ApiResponse(responseCode="401",description="Authentication required"),@ApiResponse(responseCode="403",description="Only the seller or an administrator can validate"),@ApiResponse(responseCode="404",description="Reservation not found"),@ApiResponse(responseCode="409",description="Reservation is no longer active")})
    public boolean validatePin(@PathVariable String id,@Valid @RequestBody ValidateReservationPinRequest request,Authentication auth){
        return service.validatePin(id,request.getPin(),auth.getName(),isAdmin(auth));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary="Cancel a reservation",description="The buyer, seller, or administrator may cancel an active reservation; its publication returns to AVAILABLE.")
    @ApiResponses({@ApiResponse(responseCode="204",description="Reservation cancelled"),@ApiResponse(responseCode="401",description="Authentication required"),@ApiResponse(responseCode="404",description="Reservation not found or not visible to this user"),@ApiResponse(responseCode="409",description="Reservation is no longer active")})
    public void cancel(@PathVariable String id,Authentication auth){service.cancel(id,auth.getName(),isAdmin(auth));}

    private boolean isAdmin(Authentication auth){
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch("ROLE_ADMIN"::equals);
    }
}
