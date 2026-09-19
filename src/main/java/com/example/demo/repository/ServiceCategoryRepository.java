package com.example.demo.repository;


import com.example.demo.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.deleted = false")
    List<ServiceCategory> findAllActive();

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.id = :id AND sc.deleted = false")
    Optional<ServiceCategory> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.id = :id AND sc.deleted = false AND sc.enabled = true")
    Optional<ServiceCategory> findByIdAndEnabledAndNotDeleted(@Param("id") Long id);

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.parentId IS NULL AND sc.deleted = false")
    List<ServiceCategory> findAllRootActive();

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.parentId = :parentId AND sc.deleted = false")
    List<ServiceCategory> findByParentIdAndNotDeleted(@Param("parentId") Long parentId);

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.uuid = :uuid AND sc.deleted = false")
    Optional<ServiceCategory> findByUuidAndNotDeleted(@Param("uuid") UUID uuid);

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.uuid = :uuid AND sc.deleted = false AND sc.enabled = true")
    Optional<ServiceCategory> findByUuidAndEnabledAndNotDeleted(@Param("uuid") UUID uuid);

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.parentId = :parentId AND sc.deleted = false")
    List<ServiceCategory> findByParentIdAndNotDeleted(@Param("parentId") UUID parentId);

}
