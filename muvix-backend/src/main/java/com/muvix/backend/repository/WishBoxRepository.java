package com.muvix.backend.repository;
import com.muvix.backend.model.WishBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface WishBoxRepository extends JpaRepository<WishBox,Long> {
    List<WishBox> findAllByOwnerEmailIgnoreCaseOrderByCreatedAtDesc(String email);
    java.util.Optional<WishBox> findByIdAndOwnerEmailIgnoreCase(Long id, String email);
    List<WishBox> findAllByTypeInOrderByCreatedAtDesc(List<WishBox.Type> types);
    @Query("select distinct b from WishBox b join b.publications p where p.id=:publicationId")
    List<WishBox> findAllContainingPublication(@Param("publicationId") Long publicationId);
}
