package com.example.demo.service;


import com.example.demo.dto.region.CreateRegionRequest;
import com.example.demo.dto.region.RegionMapper;
import com.example.demo.dto.region.RegionResponse;
import com.example.demo.dto.region.UpdateRegionRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Region;
import com.example.demo.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegionService {

    private final RegionRepository repository;

    public RegionService(RegionRepository repository) {
        this.repository = repository;
    }

    // find all parent region
    @Transactional(readOnly = true)
    public List<RegionResponse> findAll() {
        return repository.findAllActiveRoots()
                .stream()
                .map(RegionMapper::toResponse)
                .collect(Collectors.toList());
    }

    // find all region by uuid
    @Transactional(readOnly = true)
    public RegionResponse findByUuid(UUID uuid) {
        Region entity = getEnabledEntity(uuid);
        return RegionMapper.toResponse(entity);
    }

    // find all child-region by uuid
    @Transactional(readOnly = true)
    public List<RegionResponse> findChildren(UUID parentUuid) {
        getEnabledEntity(parentUuid);

        return repository.findActiveChildren(parentUuid)
                .stream()
                .map(RegionMapper::toResponse)
                .collect(Collectors.toList());
    }


    // admin only
    @Transactional
    public RegionResponse create(CreateRegionRequest request) {

        Region parent = null;

        if (request.getParentUuid() != null) {
            parent = getActiveEntity(request.getParentUuid());
        }

        Region entity = RegionMapper.toEntity(request, parent);

        Region saved = repository.save(entity);

        return RegionMapper.toResponse(saved);
    }

    @Transactional
    public RegionResponse update(
            UUID uuid,
            UpdateRegionRequest request
    ) {
        Region entity = getActiveEntity(uuid);

        Region parent = null;

        if (request.getParentUuid() != null) {

            parent = getActiveEntity(request.getParentUuid());

            if (parent.getUuid().equals(uuid)) {
                throw new BusinessException(
                        "Region cannot be its own parent"
                );
            }
        }

        // name and parent can update here
        RegionMapper.updateEntity(
                entity,
                request,
                parent
        );

        Region saved = repository.save(entity);

        return RegionMapper.toResponse(saved);
    }

    @Transactional
    public void enable(UUID uuid) {
        Region entity = getActiveEntity(uuid);
        entity.setEnabled(true);
        repository.save(entity);
    }

    @Transactional
    public void disable(UUID uuid) {
        Region entity = getActiveEntity(uuid);
        entity.setEnabled(false);
        repository.save(entity);
    }

    @Transactional(readOnly = true)
    public Region getEnabledAndActive(UUID uuid) {
        return repository.findByUuidAndEnabled(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Region not found or is disabled. uuid=" + uuid
                ));
    }

    private Region getEnabledEntity(UUID uuid) {
        return repository.findByUuidAndEnabled(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Region not found or is disabled. uuid=" + uuid
                ));
    }
//---------------------------------------------------------
//    this part is for user address
    @Transactional(readOnly = true)
    public Region getEnabledAndActive(Long id) {
        return repository.findByIdAndEnabled(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Region not found or is disabled. uuid=" + id
                ));
    }

    private Region getEnabledEntity(Long id) {
        return repository.findByIdAndEnabled(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Region not found or is disabled. uuid=" + id
                ));
    }
    //-----------------------------------------------------

    private Region getActiveEntity(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Region not found. uuid=" + uuid
                ));
    }
}
