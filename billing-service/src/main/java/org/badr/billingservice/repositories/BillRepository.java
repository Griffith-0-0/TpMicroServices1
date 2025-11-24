package org.badr.billingservice.repositories;

import org.badr.billingservice.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByCustomerId(Long customerId);

    @Query("SELECT DISTINCT b FROM Bill b JOIN b.productItems pi WHERE pi.productId = :productId")
    List<Bill> findByProductId(@Param("productId") Long productId);
}