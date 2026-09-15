package com.example.demo.dto.region;


import com.example.demo.model.Region;

public final class RegionMapper {

    private RegionMapper() {
    }

    public static Region toEntity(
            CreateRegionRequest request,
            Region parent
    ) {
        Region entity = new Region();

        entity.setName(request.getName());
        entity.setEnabled(request.isEnabled());
        entity.setParent(parent);

        return entity;
    }

    public static void updateEntity(
            Region entity,
            UpdateRegionRequest request,
            Region parent
    ) {
        entity.setName(request.getName());
        entity.setParent(parent);
    }

    public static RegionResponse toResponse(Region entity) {
        RegionResponse response = new RegionResponse();

        response.setUuid(entity.getUuid());
        response.setName(entity.getName());
        response.setEnabled(entity.isEnabled());

        if (entity.getParent() != null) {
            response.setParentUuid(entity.getParent().getUuid());
        }

        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }
}
//import com.example.demo.model.Region;
//
//public final class RegionMapper {
//
//    private RegionMapper() {
//    }
//
//    public static Region toEntity(CreateRegionRequest request) {
//        Region entity = new Region();
//        entity.setName(request.getName());
//        entity.setEnabled(request.isEnabled());
//        return entity;
//    }
//
//    public static void updateEntity(Region entity, UpdateRegionRequest request) {
//        entity.setName(request.getName());
//        entity.setEnabled(request.isEnabled());
//    }
//
//    public static RegionResponse toResponse(Region entity) {
//        RegionResponse response = new RegionResponse();
//        response.setId(entity.getId());
//        response.setName(entity.getName());
//        response.setEnabled(entity.isEnabled());
//        response.setCreatedAt(entity.getCreatedAt());
//        response.setUpdatedAt(entity.getUpdatedAt());
//        return response;
//    }
//}