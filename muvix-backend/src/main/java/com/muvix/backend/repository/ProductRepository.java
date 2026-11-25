package com.muvix.backend.repository;
import com.muvix.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByOwnerEmailIgnoreCaseAndState(String email, Product.State state);
    List<Product> findAllByOwnerEmailIgnoreCase(String email);
}
