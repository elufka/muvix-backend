package com.muvix.backend.repository;
import com.muvix.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
public interface ReservationRepository extends JpaRepository<Reservation,String> {
    @Query("select r from Reservation r where r.status=:status and (r.buyer.id=:userId or r.publication.seller.id=:userId) order by r.createdAt desc")
    List<Reservation> findVisibleToUser(@Param("userId") Long userId,@Param("status") Reservation.Status status);
    @Query("select r from Reservation r where r.id=:id and r.status=:status and (r.buyer.id=:userId or r.publication.seller.id=:userId)")
    Optional<Reservation> findVisibleById(@Param("id") String id,@Param("userId") Long userId,@Param("status") Reservation.Status status);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select r from Reservation r where r.id=:id")
    Optional<Reservation> findByIdForUpdate(@Param("id") String id);
}
