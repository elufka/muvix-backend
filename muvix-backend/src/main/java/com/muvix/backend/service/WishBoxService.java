package com.muvix.backend.service;

import com.muvix.backend.dto.request.CreateWishBoxRequest;
import com.muvix.backend.dto.request.UpdateWishBoxRequest;
import com.muvix.backend.dto.response.WishBoxPublicationResponse;
import com.muvix.backend.dto.response.WishBoxResponse;
import com.muvix.backend.exception.ConflictException;
import com.muvix.backend.exception.ResourceNotFoundException;
import com.muvix.backend.mapper.PublicationMapper;
import com.muvix.backend.model.Publication;
import com.muvix.backend.model.User;
import com.muvix.backend.model.WishBox;
import com.muvix.backend.repository.PublicationRepository;
import com.muvix.backend.repository.UserRepository;
import com.muvix.backend.repository.WishBoxRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishBoxService {
    private final WishBoxRepository boxes;
    private final PublicationRepository publications;
    private final UserRepository users;
    private final PublicationMapper publicationMapper;
    private final Clock businessClock;

    @Transactional
    public WishBoxResponse create(CreateWishBoxRequest request, String email) {
        User owner = findUser(email);
        WishBox box = new WishBox();
        box.setOwner(owner);
        box.setType(request.getType());
        box.setMoveInDate(request.getMoveInDate());
        box.setFutureLocation(request.getFutureLocation());
        box.setMoveOutDate(request.getMoveOutDate());
        box.setDiscountRule(request.getDiscountRule() == null ? new ArrayList<>() : request.getDiscountRule());
        box.setTakeItAllPrice(request.getTakeItAllPrice());
        box.setPublications(new LinkedHashSet<>());
        validateFields(box);
        for (Long id : request.getPublicationIds()) box.getPublications().add(findAndValidatePublication(id, box, owner));
        validateTakeAllPrice(box);
        return response(boxes.save(box));
    }

    @Transactional(readOnly=true)
    public List<WishBoxResponse> listMine(String email) {
        return boxes.findAllByOwnerEmailIgnoreCaseOrderByCreatedAtDesc(email).stream().map(this::response).toList();
    }

    @Transactional(readOnly=true)
    public List<WishBoxResponse> catalog() {
        return boxes.findAllByTypeInOrderByCreatedAtDesc(List.of(WishBox.Type.SELL, WishBox.Type.FULL_RELOCATION))
                .stream().map(this::response).toList();
    }

    @Transactional(readOnly=true)
    public WishBoxResponse getPublicOrOwned(Long id, String email) {
        WishBox box = boxes.findById(id).orElseThrow(() -> new ResourceNotFoundException("Box not found"));
        User actor = email == null ? null : users.findByEmailIgnoreCase(email).orElse(null);
        boolean ownerOrAdmin = actor != null && (actor.isAdmin() || actor.getId().equals(box.getOwner().getId()));
        boolean publicType = box.getType() == WishBox.Type.SELL || box.getType() == WishBox.Type.FULL_RELOCATION;
        if (!ownerOrAdmin && !publicType) throw new ResourceNotFoundException("Box not found");
        return response(box);
    }

    @Transactional
    public WishBoxResponse update(Long id, UpdateWishBoxRequest request, String email) {
        WishBox box = ownedBox(id, email);
        if (request.getMoveInDate() != null) box.setMoveInDate(request.getMoveInDate());
        if (request.getFutureLocation() != null) box.setFutureLocation(request.getFutureLocation());
        if (request.getMoveOutDate() != null) box.setMoveOutDate(request.getMoveOutDate());
        if (request.getDiscountRule() != null) box.setDiscountRule(request.getDiscountRule());
        if (request.getTakeItAllPrice() != null) box.setTakeItAllPrice(request.getTakeItAllPrice());
        validateFields(box);
        validateTakeAllPrice(box);
        return response(box);
    }

    @Transactional
    public WishBoxResponse addPublication(Long id, Long publicationId, String email) {
        WishBox box = ownedBox(id, email);
        User owner = box.getOwner();
        Publication publication = findAndValidatePublication(publicationId, box, owner);
        box.getPublications().add(publication);
        validateTakeAllPrice(box);
        return response(box);
    }

    @Transactional
    public WishBoxResponse removePublication(Long id, Long publicationId, String email) {
        WishBox box = ownedBox(id, email);
        box.getPublications().removeIf(p -> p.getId().equals(publicationId));
        validateTakeAllPrice(box);
        return response(box);
    }

    @Transactional
    public void delete(Long id, String email) { boxes.delete(ownedBox(id, email)); }

    private Publication findAndValidatePublication(Long id, WishBox box, User owner) {
        Publication p = publications.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publication not found"));
        if (p.getState() != Publication.State.AVAILABLE) throw new ConflictException("Only available publications can be added to a box");
        boolean isOwner = p.getSeller().getId().equals(owner.getId());
        if (box.getType() == WishBox.Type.SELL && !isOwner) throw new AccessDeniedException("A sell box can contain only your publications");
        if (box.getType() == WishBox.Type.PURCHASE && isOwner) throw new ConflictException("A purchase box must contain publications from other sellers");
        return p;
    }

    private void validateFields(WishBox b) {
        boolean purchase = b.getType() == WishBox.Type.PURCHASE || b.getType() == WishBox.Type.FULL_RELOCATION;
        boolean sell = b.getType() == WishBox.Type.SELL || b.getType() == WishBox.Type.FULL_RELOCATION;
        if (purchase && (b.getMoveInDate() == null || b.getFutureLocation() == null || b.getFutureLocation().isBlank()))
            throw new ConflictException("Purchase boxes require moveInDate and futureLocation");
        if (sell && (b.getMoveOutDate() == null || b.getDiscountRule() == null || b.getDiscountRule().isEmpty() || b.getTakeItAllPrice() == null))
            throw new ConflictException("Sell boxes require moveOutDate, discountRule, and takeItAllPrice");
        if (b.getMoveInDate() != null && b.getMoveInDate().isBefore(LocalDate.now(businessClock)))
            throw new ConflictException("moveInDate cannot be in the past");
        if (b.getMoveOutDate() != null && b.getMoveOutDate().isBefore(LocalDate.now(businessClock)))
            throw new ConflictException("moveOutDate cannot be in the past");
        if (sell && b.getDiscountRule().stream().reduce(BigDecimal.ZERO, BigDecimal::add).compareTo(new BigDecimal("100")) > 0)
            throw new ConflictException("Cumulative discountRule cannot exceed 100 percent");
    }

    private void validateTakeAllPrice(WishBox b) {
        if (b.getTakeItAllPrice() == null) return;
        BigDecimal ownedTotal = b.getPublications().stream()
                .filter(p -> p.getSeller().getId().equals(b.getOwner().getId()))
                .map(Publication::getPrice).filter(p -> p != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (b.getTakeItAllPrice().compareTo(ownedTotal) >= 0)
            throw new ConflictException("takeItAllPrice must be lower than the sum of your sell publications in the box");
    }

    private WishBoxResponse response(WishBox b) {
        WishBoxResponse r = new WishBoxResponse();
        r.setId(b.getId());
        r.setOwnerId(b.getOwner().getId());
        r.setType(b.getType());
        r.setMoveInDate(b.getMoveInDate());
        r.setFutureLocation(b.getFutureLocation());
        r.setMoveOutDate(b.getMoveOutDate());
        r.setDiscountRule(new ArrayList<>(b.getDiscountRule()));
        r.setTakeItAllPrice(b.getTakeItAllPrice());
        BigDecimal original = BigDecimal.ZERO, current = BigDecimal.ZERO;
        List<WishBoxPublicationResponse> items = new ArrayList<>();
        for (Publication p : b.getPublications()) {
            BigDecimal base = p.getPrice() == null ? BigDecimal.ZERO : p.getPrice();
            boolean sellItem = p.getSeller().getId().equals(b.getOwner().getId()) && b.getType() != WishBox.Type.PURCHASE;
            BigDecimal itemPrice = sellItem ? discountedPrice(base, b) : base;
            original = original.add(base); current = current.add(itemPrice);
            WishBoxPublicationResponse item = new WishBoxPublicationResponse();
            item.setPublication(publicationMapper.toResponse(p));
            item.setCurrentBoxPrice(itemPrice);
            items.add(item);
        }
        r.setPublications(items);
        r.setOriginalTotalPrice(original);
        r.setCurrentTotalPrice(current);
        return r;
    }

    private BigDecimal discountedPrice(BigDecimal base, WishBox box) {
        if (box.getMoveOutDate() == null || box.getDiscountRule().isEmpty()) return base;
        long days = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(businessClock), box.getMoveOutDate());
        if (days <= 0 || days > box.getDiscountRule().size()) return base;
        int increments = box.getDiscountRule().size() - (int) days + 1;
        BigDecimal percent = box.getDiscountRule().subList(0, increments).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return base.multiply(BigDecimal.ONE.subtract(percent.movePointLeft(2))).setScale(2, RoundingMode.HALF_UP);
    }

    private WishBox ownedBox(Long id, String email) {
        WishBox b = boxes.findById(id).orElseThrow(() -> new ResourceNotFoundException("Box not found"));
        User actor = findUser(email);
        if (!b.getOwner().getId().equals(actor.getId()) && !actor.isAdmin()) throw new ResourceNotFoundException("Box not found");
        return b;
    }
    private User findUser(String email) {
        return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
