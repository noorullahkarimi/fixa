package com.example.demo.repository;
import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.uuid = :uuid AND o.deleted = false")
    Optional<Order> findByUuidAndNotDeleted(@Param("uuid") UUID uuid);

    @Query("SELECT o FROM Order o WHERE o.deleted = false")
    List<Order> findAllActive();

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.deleted = false")
    List<Order> findByCustomerIdAndNotDeleted(@Param("customerId") Long customerId);


    @Query("SELECT o FROM Order o WHERE o.orderCode = :orderCode AND o.deleted = false")
    Optional<Order> findByOrderCodeAndNotDeleted(@Param("orderCode") String orderCode);
}
