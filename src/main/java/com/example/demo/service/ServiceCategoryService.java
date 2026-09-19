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
import java.util.UUID;
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
                    ServiceCategoryResponse rootDto =
                            ServiceCategoryMapper.toResponse(root);

                    List<ServiceCategoryResponse> children =
                            repository.findByParentIdAndNotDeleted(root.getUuid())
                                    .stream()
                                    .map(ServiceCategoryMapper::toResponse)
                                    .collect(Collectors.toList());

                    rootDto.setChildren(children);

                    return rootDto;
                })
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ServiceCategoryResponse findByUuid(UUID uuid) {
        ServiceCategory entity = getActiveEntity(uuid);
        ServiceCategoryResponse response = ServiceCategoryMapper.toResponse(entity);

        if (entity.getParentId() == null) {
            List<ServiceCategoryResponse> children = repository
                    .findByParentIdAndNotDeleted(entity.getUuid())
                    .stream()
                    .map(ServiceCategoryMapper::toResponse)
                    .collect(Collectors.toList());
            response.setChildren(children);
        }

        return response;
    }

    // creating new service
    @Transactional
    public ServiceCategoryResponse create(CreateServiceCategoryRequest request) {
        ServiceCategory parent = validateParent(request.getParentId());

        ServiceCategory entity = ServiceCategoryMapper.toEntity(request, parent);
        ServiceCategory saved = repository.save(entity);

        return ServiceCategoryMapper.toResponse(saved);
    }

    //updat a service row
    @Transactional
    public ServiceCategoryResponse update(UUID uuid, UpdateServiceCategoryRequest request) {
        ServiceCategory entity = getActiveEntity(uuid);

        if (request.getParentId() != null && request.getParentId().equals(uuid)) {
            throw new BusinessException("A category cannot be its own parent");
        }

        ServiceCategory parent = validateParent(request.getParentId());

        ServiceCategoryMapper.updateEntity(entity, request, parent);
        ServiceCategory saved = repository.save(entity);

        return ServiceCategoryMapper.toResponse(saved);
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

    @Transactional(readOnly = true)
    public ServiceCategory getEnabledLeafAndActive(UUID uuid) {
        ServiceCategory category = repository.findByUuidAndEnabledAndNotDeleted(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ServiceCategory not found or is disabled. uuid=" + uuid));

        if (category.getParentId() == null) {
            throw new BusinessException(
                    "Only sub-categories can be ordered. Root category is not allowed. uuid=" + uuid);
        }

        return category;
    }
    // -------------------- private helpers --------------------

    private ServiceCategory getActiveEntity(UUID uuid) {
        return repository.findByUuidAndNotDeleted(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ServiceCategory not found. uuid=" + uuid));
    }

    private ServiceCategory getActiveEntity(Long id) {
        return repository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ServiceCategory not found. id=" + id));
    }

    private void validateParent(Long parentId) {

        // mean it is category not sub
        if (parentId == null) {
            return;
        }
        //check if there is sub-category so check for parent-id there is
        ServiceCategory parent = repository.findByIdAndNotDeleted(parentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parent ServiceCategory not found. id=" + parentId));

        // prevent to make level third of category
        if (parent.getParentId() != null) {
            throw new BusinessException(
                    "Only two levels are supported. Cannot create a sub-category under another sub-category");
        }
    }

    private ServiceCategory validateParent(UUID parentUuid) {

        if (parentUuid == null) {
            return null;
        }

        ServiceCategory parent = repository.findByUuidAndNotDeleted(parentUuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parent ServiceCategory not found. uuid=" + parentUuid));

        if (parent.getParentId() != null) {
            throw new BusinessException(
                    "Only two levels are supported. Cannot create a sub-category under another sub-category");
        }

        return parent;
    }
}
