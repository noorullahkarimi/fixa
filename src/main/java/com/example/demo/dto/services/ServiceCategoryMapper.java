package com.example.demo.dto.services;


import com.example.demo.model.ServiceCategory;

public final class ServiceCategoryMapper {

    private ServiceCategoryMapper() {
    }

    public static ServiceCategory toEntity(
            CreateServiceCategoryRequest request,
            ServiceCategory parent) {

        ServiceCategory entity = new ServiceCategory();
        entity.setName(request.getName());
        entity.setEnabled(request.isEnabled());
        entity.setParentId(parent != null ? parent.getUuid() : null);

        return entity;
    }

    public static void updateEntity(
            ServiceCategory entity,
            UpdateServiceCategoryRequest request,
            ServiceCategory parent) {

        entity.setName(request.getName());
        entity.setEnabled(request.isEnabled());
        entity.setParentId(parent != null ? parent.getUuid() : null);
    }

    public static ServiceCategoryResponse toResponse(ServiceCategory entity) {
        ServiceCategoryResponse response = new ServiceCategoryResponse();

        response.setUuid(entity.getUuid());
        response.setName(entity.getName());
        response.setEnabled(entity.isEnabled());
        response.setParentId(entity.getParentId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }
}