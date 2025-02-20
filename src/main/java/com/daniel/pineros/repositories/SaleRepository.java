package com.daniel.pineros.repositories;

import com.daniel.pineros.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,String> {
    List<Sale> findByClient_UserName(String userName);
}
