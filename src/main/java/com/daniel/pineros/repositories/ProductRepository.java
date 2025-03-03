package com.daniel.pineros.repositories;

import com.daniel.pineros.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {
    List<Product> findByCategoryAndIdNot(String category, String ProductId);
    List<Product> findFirst4ByOrderByPriceAsc();
}
