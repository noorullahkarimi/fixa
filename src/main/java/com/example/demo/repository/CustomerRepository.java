package com.example.demo.repository;

import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.deleted = false")
    Optional<Customer> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT c FROM Customer c WHERE c.uuid = :uuid AND c.deleted = false")
    Optional<Customer> findByUuidAndNotDeleted(@Param("uuid") UUID uuid);

    @Query("SELECT c FROM Customer c WHERE c.uuid = :uuid AND c.deleted = false")
    Customer findByUuid(@Param("uuid") UUID uuid);

    boolean existsByNationalCodeAndDeletedFalse(String nationalCode);

    boolean existsByMobile(String mobile);

    Customer findByMobile(String s);
}
