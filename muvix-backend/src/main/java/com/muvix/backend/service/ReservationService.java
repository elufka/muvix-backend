package com.muvix.backend.service;

import com.muvix.backend.dto.request.CreateReservationRequest;
import com.muvix.backend.dto.response.ReservationResponse;
import com.muvix.backend.exception.ConflictException;
import com.muvix.backend.exception.ResourceNotFoundException;
import com.muvix.backend.mapper.PublicationMapper;
import com.muvix.backend.mapper.UserMapper;
import com.muvix.backend.model.Publication;
import com.muvix.backend.model.Reservation;
import com.muvix.backend.model.User;
import com.muvix.backend.repository.PublicationRepository;
import com.muvix.backend.repository.ReservationRepository;
import com.muvix.backend.repository.UserRepository;
import com.muvix.backend.repository.WishBoxRepository;
import com.muvix.backend.model.WishBox;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservations;
    private final PublicationRepository publications;
    private final UserRepository users;
    private final WishBoxRepository boxes;
    private final PublicationMapper publicationMapper;
    private final UserMapper userMapper;

    @Transactional
    public ReservationResponse create(CreateReservationRequest request,String email){
        User buyer=findUser(email);
        if(!buyer.getId().equals(request.getUserId())) throw new AccessDeniedException("Reservation buyer must be the authenticated user");
        Long publicationId=parsePublicationId(request.getPublicationId());
        Publication publication=publications.findByIdForUpdate(publicationId)
                .orElseThrow(()->new ResourceNotFoundException("Publication not found"));
        if(publication.getState()!=Publication.State.AVAILABLE) throw new ConflictException("Publication is not available for reservation");
        if(request.getBoxId()!=null){
            WishBox box=boxes.findByIdAndOwnerEmailIgnoreCase(request.getBoxId(),email)
                    .orElseThrow(()->new ResourceNotFoundException("Purchase box not found"));
            if(box.getType()==WishBox.Type.SELL || box.getPublications().stream().noneMatch(p->p.getId().equals(publicationId)))
                throw new ConflictException("The publication is not part of this purchase box");
            if(box.getMoveInDate()!=null && (request.getPickupDate()==null || request.getPickupDate().toLocalDate().isBefore(box.getMoveInDate())))
                throw new ConflictException("Pickup must be scheduled on or after the box move-in date");
        }
        publication.setState(Publication.State.RESERVED);
        Reservation reservation=new Reservation();
        reservation.setBuyer(buyer);
        reservation.setPublication(publication);
        reservation.setPrice(request.getPrice());
        reservation.setPickupDate(request.getPickupDate());
        reservation.setPin(request.getPin());
        publications.save(publication);
        return toResponse(reservations.save(reservation));
    }

    @Transactional(readOnly=true)
    public List<ReservationResponse> listVisible(String email){
        User user=findUser(email);
        return reservations.findVisibleToUser(user.getId(),Reservation.Status.ACTIVE).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly=true)
    public ReservationResponse getVisible(String id,String email){
        User user=findUser(email);
        return reservations.findVisibleById(id,user.getId(),Reservation.Status.ACTIVE).map(this::toResponse)
                .orElseThrow(()->new ResourceNotFoundException("Reservation not found"));
    }

    @Transactional
    public boolean validatePin(String id,String suppliedPin,String email,boolean admin){
        User actor=findUser(email);
        Reservation reservation=reservations.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundException("Reservation not found"));
        if(reservation.getStatus()!=Reservation.Status.ACTIVE) throw new ConflictException("Reservation is no longer active");
        if(!admin&&!reservation.getPublication().getSeller().getId().equals(actor.getId()))
            throw new AccessDeniedException("Only the seller may validate the reservation PIN");
        if(reservation.getPin()==null||suppliedPin==null||!reservation.getPin().equals(suppliedPin)) return false;
        Publication publication=publications.findByIdForUpdate(reservation.getPublication().getId())
                .orElseThrow(()->new ResourceNotFoundException("Publication not found"));
        publication.setState(Publication.State.SOLD);
        reservation.setStatus(Reservation.Status.COMPLETED);
        return true;
    }

    @Transactional
    public void cancel(String id,String email,boolean admin){
        User actor=findUser(email);
        Reservation reservation=reservations.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundException("Reservation not found"));
        if(reservation.getStatus()!=Reservation.Status.ACTIVE) throw new ConflictException("Reservation is no longer active");
        boolean buyer=reservation.getBuyer().getId().equals(actor.getId());
        boolean seller=reservation.getPublication().getSeller().getId().equals(actor.getId());
        if(!admin&&!buyer&&!seller) throw new ResourceNotFoundException("Reservation not found");
        Publication publication=publications.findByIdForUpdate(reservation.getPublication().getId())
                .orElseThrow(()->new ResourceNotFoundException("Publication not found"));
        publication.setState(Publication.State.AVAILABLE);
        reservation.setStatus(Reservation.Status.CANCELLED);
    }

    private User findUser(String email){return users.findByEmailIgnoreCase(email).orElseThrow(()->new ResourceNotFoundException("User not found"));}
    private Long parsePublicationId(String id){
        try{return Long.valueOf(id);}catch(NumberFormatException ex){throw new ResourceNotFoundException("Publication not found");}
    }
    private ReservationResponse toResponse(Reservation reservation){
        ReservationResponse response=new ReservationResponse();
        response.setId(reservation.getId());
        response.setPublicationId(reservation.getPublication().getId().toString());
        response.setUserId(reservation.getBuyer().getId());
        response.setPrice(reservation.getPrice());
        response.setPickupDate(reservation.getPickupDate());
        response.setPin(reservation.getPin());
        response.setStatus(reservation.getStatus());
        response.setUser(userMapper.toResponse(reservation.getBuyer()));
        response.setPublication(publicationMapper.toResponse(reservation.getPublication()));
        return response;
    }
}
