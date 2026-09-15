package com.example.demo.service;

import com.example.demo.dto.services.CreateServiceCategoryRequest;
import com.example.demo.dto.services.ServiceCategoryMapper;
import com.example.demo.dto.services.ServiceCategoryResponse;
import com.example.demo.dto.services.UpdateServiceCategoryRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ServiceCategory;
import com.example.demo.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCategoryService {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryService(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ServiceCategoryResponse> findAll() {
        List<ServiceCategory> roots = repository.findAllRootActive();

        return roots.stream()
                .map(root -> {
                    ServiceCategoryResponse rootDto = ServiceCategoryMapper.toResponse(root);
                    List<ServiceCategoryResponse> children = repository
                            .findByParentIdAndNotDeleted(root.getId())
                            .stream()
                            .map(ServiceCategoryMapper::toResponse)
                            .collect(Collectors.toList());
                    rootDto.setChildren(children);
                    return rootDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServiceCategoryResponse findById(Long id) {
        ServiceCategory entity = getActiveEntity(id);
        ServiceCategoryResponse response = ServiceCategoryMapper.toResponse(entity);


        if (entity.getParentId() == null) {
            List<ServiceCategoryResponse> children = repository
                    .findByParentIdAndNotDeleted(entity.getId())
                    .stream()
                    .map(ServiceCategoryMapper::toResponse)
                    .collect(Collectors.toList());
            response.setChildren(children);
        }
        return response;
    }

    @Transactional
    public ServiceCategoryResponse create(CreateServiceCategoryRequest request) {
        validateParent(request.getParentId());

        ServiceCategory entity = ServiceCategoryMapper.toEntity(request);
        ServiceCategory saved = repository.save(entity);
        return ServiceCategoryMapper.toResponse(saved);
    }

    @Transactional
    public ServiceCategoryResponse update(Long id, UpdateServiceCategoryRequest request) {
        ServiceCategory entity = getActiveEntity(id);

        // Prevent setting a category as its own parent
        if (request.getParentId() != null && request.getParentId().equals(id)) {
            throw new BusinessException("A category cannot be its own parent");
        }

        validateParent(request.getParentId());

        ServiceCategoryMapper.updateEntity(entity, request);
        ServiceCategory saved = repository.save(entity);
        return ServiceCategoryMapper.toResponse(saved);
    }

    @Transactional
    public void softDelete(Long id) {
        ServiceCategory entity = getActiveEntity(id);
        entity.setDeleted(true);
        repository.save(entity);


        List<ServiceCategory> children = repository.findByParentIdAndNotDeleted(id);
        for (ServiceCategory child : children) {
            child.setDeleted(true);
        }
        repository.saveAll(children);
    }

    @Transactional(readOnly = true)
    public ServiceCategory getEnabledLeafAndActive(Long id) {
        ServiceCategory category = repository.findByIdAndEnabledAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ServiceCategory not found or is disabled. id=" + id));

        if (category.getParentId() == null) {
            throw new BusinessException("Only sub-categories can be ordered. Root category is not allowed. id=" + id);
        }

        return category;
    }

    // -------------------- private helpers --------------------

    private ServiceCategory getActiveEntity(Long id) {
        return repository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ServiceCategory not found. id=" + id));
    }

    private void validateParent(Long parentId) {
        if (parentId == null) {
            return;
        }

        ServiceCategory parent = repository.findByIdAndNotDeleted(parentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parent ServiceCategory not found. id=" + parentId));


        if (parent.getParentId() != null) {
            throw new BusinessException(
                    "Only two levels are supported. Cannot create a sub-category under another sub-category");
        }
    }
}
