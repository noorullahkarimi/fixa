package com.example.demo.service;
import com.example.demo.dto.customer.CreateCustomerRequest;
import com.example.demo.dto.customer.CustomerMapper;
import com.example.demo.dto.customer.CustomerResponse;
import com.example.demo.dto.customer.UpdateCustomerRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        Customer entity = getActiveEntity(id);
        return CustomerMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public CustomerResponse findByUuid(UUID uuid) {
        Customer entity = customerRepository.findByUuid(uuid);
        return CustomerMapper.toResponse(entity);
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        // check if user with national code exist
        if (customerRepository.existsByNationalCodeAndDeletedFalse(request.getNationalCode())) {
            throw new BusinessException("Customer with this nationalCode already exists");
        }
        // check if user with phone number exist
        if (customerRepository.existsByMobile(request.getMobile())) {
            throw new BusinessException("mobile already exists");
        }

        Customer entity = CustomerMapper.toEntity(request);
        Customer saved = customerRepository.save(entity);
        return CustomerMapper.toResponse(saved);
    }

    // find customer activated
    @Transactional(readOnly = true)
    public Customer getActiveEntity(Long id) {
        return customerRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found. id=" + id));
    }

    @Transactional(readOnly = true)
    public Customer getActiveEntity(UUID uuid) {
        return customerRepository.findByUuidAndNotDeleted(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found. id=" + uuid));
    }
}