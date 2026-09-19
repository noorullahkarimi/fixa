package com.example.demo.service;
import com.example.demo.dto.order.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.OrderCodeRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final ServiceCategoryService serviceCategoryService;
    private final CustomerService customerService;
    private final AddressService addressService;
    private final OrderStatusService orderStatusService;
    private final OrderCodeRepository orderCodeRepository;

    // it used for making order code
    private static final DateTimeFormatter CODE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public OrderService(OrderRepository orderRepository,
                        OrderStatusHistoryRepository historyRepository,
                        ServiceCategoryService serviceCategoryService,
                        CustomerService customerService,
                        AddressService addressService,
                        OrderStatusService orderStatusService, OrderCodeRepository orderCodeRepository) {
        this.orderRepository = orderRepository;
        this.historyRepository = historyRepository;
        this.serviceCategoryService = serviceCategoryService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.orderStatusService = orderStatusService;
        this.orderCodeRepository = orderCodeRepository;
    }

    @Transactional
    public CreateOrderResponse create(CreateOrderRequest request) {

        ServiceCategory serviceCategory =
                serviceCategoryService.getEnabledLeafAndActive(request.getServiceCategoryUuid());

        Customer customer =
                customerService.getActiveEntity(request.getCustomerUuid());

        Address address =
                addressService.getActiveEntityBelongingToCustomer(
                        request.getAddressUuid(),
                        request.getCustomerUuid());

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
    public OrderResponse changeStatus(UUID orderUuid, ChangeOrderStatusRequest request) {
        Order order = getActiveEntity(orderUuid);

        orderStatusService.changeStatus(
                order,
                request.getNewStatus(),
                request.getComment());

        Order saved = orderRepository.save(order);
        return OrderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderStatusHistoryResponse> getStatusHistory(UUID orderUuid) {
        Order order = getActiveEntity(orderUuid);

        return historyRepository.findByOrderIdOrderByChangedAtAsc(order.getId())
                .stream()
                .map(OrderStatusHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    private Order getActiveEntity(UUID uuid) {
        return orderRepository.findByUuidAndNotDeleted(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found. uuid=" + uuid));
    }

    @Transactional(readOnly = true)
    public OrderResponse findByUuid(UUID uuid) {
        Order entity = getActiveEntity(uuid);
        return OrderMapper.toResponse(entity);
    }

    // create order code
    //  add ( last order number + 1 , date ) e.g. 20260916-00001
    private String generateOrderCode() {
        LocalDate today = LocalDate.now();

        orderCodeRepository.incrementSequence(today);

        Long nextSequence = orderCodeRepository.findLastCode(today);

        String datePrefix = today.format(CODE_DATE_FORMATTER);

        return String.format("%s-%05d", datePrefix, nextSequence);
    }
}