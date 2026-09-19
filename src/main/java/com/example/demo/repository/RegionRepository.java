package com.example.demo.repository;


import com.example.demo.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("""
        SELECT r
        FROM Region r
        WHERE r.enabled = true
        AND r.parent IS NULL
        ORDER BY r.name
        """)
    List<Region> findAllActiveRoots();

    @Query("""
        SELECT r
        FROM Region r
        WHERE r.enabled = true
        AND r.parent.uuid = :parentUuid
        ORDER BY r.name
        """)
    List<Region> findActiveChildren(
            @Param("parentUuid") UUID parentUuid
    );

    @Query("""
        SELECT r
        FROM Region r
        WHERE r.uuid = :uuid
        """)
    Optional<Region> findByUuid(
            @Param("uuid") UUID uuid
    );

    //all user should be able to add all regions but when we get order, we just get enable region
    @Query("""
        SELECT r
        FROM Region r
        WHERE r.id = :id
        """)
    Optional<Region> findByIdAndEnabled(
            @Param("id") Long id
    );

    @Query("""
        SELECT r
        FROM Region r
        WHERE r.uuid = :uuid
        AND r.enabled = true
        """)
    Optional<Region> findByUuidAndEnabled(
            @Param("uuid") UUID uuid
    );

    @Query("""
        SELECT r
        FROM Region r
        WHERE r.uuid = :uuid
        AND r.enabled = true
        AND r.parent IS NULL
        """)
    Optional<Region> findActiveRootByUuid(
            @Param("uuid") UUID uuid
    );
}

