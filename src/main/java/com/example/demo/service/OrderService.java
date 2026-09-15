package com.example.demo.service;
import com.example.demo.dto.order.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.ServiceCategory;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final ServiceCategoryService serviceCategoryService;
    private final CustomerService customerService;
    private final AddressService addressService;
    private final OrderStatusService orderStatusService;

    private static final DateTimeFormatter CODE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public OrderService(OrderRepository orderRepository,
                        OrderStatusHistoryRepository historyRepository,
                        ServiceCategoryService serviceCategoryService,
                        CustomerService customerService,
                        AddressService addressService,
                        OrderStatusService orderStatusService) {
        this.orderRepository = orderRepository;
        this.historyRepository = historyRepository;
        this.serviceCategoryService = serviceCategoryService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.orderStatusService = orderStatusService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAllActive()
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order entity = getActiveEntity(id);
        return OrderMapper.toResponse(entity);
    }


    @Transactional
    public CreateOrderResponse create(CreateOrderRequest request) {

        ServiceCategory serviceCategory = serviceCategoryService.getEnabledLeafAndActive(request.getServiceCategoryId());

        Customer customer = customerService.getActiveEntity(request.getCustomerId());

        Address address = addressService.getActiveEntityBelongingToCustomer(request.getAddressId(), request.getCustomerId());

        if (!address.getRegion().isEnabled()) {
            throw new ResourceNotFoundException(
                    "Region of the address is disabled or deleted. regionId="
                            + address.getRegion().getId());
        }

        String orderCode = generateOrderCode();

        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setRequestedDate(request.getRequestedDate());
        order.setCustomer(customer);
        order.setAddress(address);
        order.setServiceCategory(serviceCategory);
        order.setStatus(OrderStatus.FINAL_ORDER);

        Order savedOrder = orderRepository.save(order);

        orderStatusService.recordInitialStatus(savedOrder);

        return OrderMapper.toCreateResponse(savedOrder);
    }

    @Transactional
    public OrderResponse changeStatus(Long orderId, ChangeOrderStatusRequest request) {
        Order order = getActiveEntity(orderId);
        orderStatusService.changeStatus(order, request.getNewStatus(), request.getComment());
        Order saved = orderRepository.save(order);
        return OrderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderStatusHistoryResponse> getStatusHistory(Long orderId) {
        getActiveEntity(orderId);
        return historyRepository.findByOrderIdOrderByChangedAtAsc(orderId)
                .stream()
                .map(OrderStatusHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void softDelete(Long id) {
        Order entity = getActiveEntity(id);
        entity.setDeleted(true);
        orderRepository.save(entity);
    }

    private Order getActiveEntity(Long id) {
        return orderRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found. id=" + id));
    }


    private String generateOrderCode() {
        String datePrefix = LocalDate.now().format(CODE_DATE_FORMATTER);
        long countToday = orderRepository.countByOrderCodeStartingWith(datePrefix);
        long nextSequence = countToday + 1;
        return String.format("%s-%05d", datePrefix, nextSequence);
    }
}