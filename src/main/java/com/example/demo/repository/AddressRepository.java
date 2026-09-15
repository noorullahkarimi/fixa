package com.example.demo.repository;
import com.example.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.deleted = false")
    List<Address> findAllActive();

    @Query("SELECT a FROM Address a WHERE a.id = :id AND a.deleted = false")
    Optional<Address> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND a.deleted = false")
    List<Address> findByCustomerIdAndNotDeleted(@Param("customerId") Long customerId);

    @Query("""
           SELECT a FROM Address a
           WHERE a.id = :id
             AND a.customer.id = :customerId
             AND a.deleted = false
           """)
    Optional<Address> findByIdAndCustomerIdAndNotDeleted(@Param("id") Long id,
                                                         @Param("customerId") Long customerId);
}
