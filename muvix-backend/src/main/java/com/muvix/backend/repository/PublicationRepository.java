package com.muvix.backend.repository;
import com.muvix.backend.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Publication p where p.id=:id")
    java.util.Optional<Publication> findByIdForUpdate(@Param("id") Long id);
    Page<Publication> findAllByState(Publication.State state,Pageable pageable);
    List<Publication> findAllBySellerEmailIgnoreCase(String email);
    List<Publication> findAllBySellerEmailIgnoreCaseAndStateNot(String email, Publication.State state);
    List<Publication> findAllBySellerEmailIgnoreCaseAndState(String email, Publication.State state);
    List<Publication> findAllBySellerEmailIgnoreCaseAndTypeAndState(String email, Publication.Type type, Publication.State state);
}
