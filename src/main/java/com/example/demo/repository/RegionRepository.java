package com.example.demo.repository;

import com.example.demo.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("SELECT r FROM Region r WHERE r.deleted = false")
    List<Region> findAllActive();

    @Query("SELECT r FROM Region r WHERE r.id = :id AND r.deleted = false")
    Optional<Region> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT r FROM Region r WHERE r.id = :id AND r.deleted = false AND r.enabled = true")
    Optional<Region> findByIdAndEnabledAndNotDeleted(@Param("id") Long id);
}
