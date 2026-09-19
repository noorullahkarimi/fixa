package com.example.demo.order;

import com.example.demo.dto.address.CreateAddressRequest;
import com.example.demo.dto.address.UpdateAddressRequest;
import com.example.demo.dto.customer.CreateCustomerRequest;
import com.example.demo.dto.order.ChangeOrderStatusRequest;
import com.example.demo.dto.order.CreateOrderRequest;
import com.example.demo.dto.services.CreateServiceCategoryRequest;
import com.example.demo.dto.services.UpdateServiceCategoryRequest;
import com.example.demo.enums.OrderStatus;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ServiceCategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusHistoryRepository historyRepository;

    private Customer customer;
    private Region region;
    private Address address;
    private ServiceCategory category;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setFirstName("علی");
        customer.setLastName("رضایی");
        customer.setMobile("09123456789");
        customer.setNationalCode("1234567890");

        customer = customerRepository.save(customer);

        region = new Region();
        region.setName("تهران");
        region.setEnabled(true);

        region = regionRepository.save(region);

        address = new Address();
        address.setDetails("خیابان ولیعصر");
        address.setCustomer(customer);
        address.setRegion(region);
        address.setLatitude(35.7);
        address.setLongitude(51.4);

        address = addressRepository.save(address);

        category = new ServiceCategory();
        category.setName("کارواش");
        category.setEnabled(true);
        category.setParentId(UUID.randomUUID());

        category = categoryRepository.save(category);
    }

    @Test
    void shouldCreateOrder() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest();

        request.setRequestedDate(
                LocalDate.now().plusDays(1));

        request.setCustomerUuid(customer.getUuid());
        request.setAddressUuid(address.getUuid());
        request.setServiceCategoryUuid(category.getUuid());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.orderCode")
                        .value(org.hamcrest.Matchers.matchesPattern(
                                "\\d{8}-\\d{5}"
                        )));

        assertThat(orderRepository.findAll())
                .hasSize(1);

        Order order =
                orderRepository.findAll().get(0);

        assertThat(order.getCustomer().getUuid())
                .isEqualTo(customer.getUuid());

        assertThat(order.getAddress().getUuid())
                .isEqualTo(address.getUuid());

        assertThat(order.getServiceCategory().getUuid())
                .isEqualTo(category.getUuid());

        assertThat(order.getStatus())
                .isEqualTo(OrderStatus.FINAL_ORDER);

        assertThat(historyRepository.findAll())
                .hasSize(1);

        OrderStatusHistory history =
                historyRepository.findAll().get(0);

        assertThat(history.getFromStatus())
                .isNull();

        assertThat(history.getToStatus())
                .isEqualTo(OrderStatus.FINAL_ORDER);
    }

    @Test
    void shouldFindOrderByUuid() throws Exception {

        Order order = createOrder();

        mockMvc.perform(
                        get("/api/orders/{uuid}",
                                order.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid")
                        .value(order.getUuid().toString()))
                .andExpect(jsonPath("$.orderCode")
                        .value(order.getOrderCode()))
                .andExpect(jsonPath("$.customerUuid")
                        .value(customer.getUuid().toString()))
                .andExpect(jsonPath("$.addressUuid")
                        .value(address.getUuid().toString()))
                .andExpect(jsonPath("$.serviceCategoryUuid")
                        .value(category.getUuid().toString()));
    }

    @Test
    void shouldChangeOrderStatus() throws Exception {

        Order order = createOrder();

        assertThat(historyRepository.findAll())
                .hasSize(1);

        ChangeOrderStatusRequest request =
                new ChangeOrderStatusRequest();

        request.setNewStatus(
                OrderStatus.TECHNICIAN_ACCEPTED);

        request.setComment("تکنسین قبول کرد");

        mockMvc.perform(
                        patch("/api/orders/{uuid}/status",
                                order.getUuid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("TECHNICIAN_ACCEPTED"));

        Order updated =
                orderRepository.findById(order.getId()).orElseThrow();

        assertThat(updated.getStatus())
                .isEqualTo(OrderStatus.TECHNICIAN_ACCEPTED);

        assertThat(historyRepository.findAll())
                .hasSize(2);

        OrderStatusHistory history =
                historyRepository.findAll()
                        .stream()
                        .filter(h ->
                                h.getToStatus()
                                        == OrderStatus.TECHNICIAN_ACCEPTED)
                        .findFirst()
                        .orElseThrow();

        assertThat(history.getFromStatus())
                .isEqualTo(OrderStatus.FINAL_ORDER);

        assertThat(history.getComment())
                .isEqualTo("تکنسین قبول کرد");
    }

    @Test
    void shouldRejectInvalidStatusTransition() throws Exception {

        Order order = createOrder();

        ChangeOrderStatusRequest request =
                new ChangeOrderStatusRequest();

        request.setNewStatus(
                OrderStatus.ORDER_COMPLETED);

        request.setComment("انتقال نامعتبر");

        mockMvc.perform(
                        patch("/api/orders/{uuid}/status",
                                order.getUuid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Invalid status transition from FINAL_ORDER to ORDER_COMPLETED"
                        ));

        Order unchanged =
                orderRepository.findById(order.getId()).orElseThrow();

        assertThat(unchanged.getStatus())
                .isEqualTo(OrderStatus.FINAL_ORDER);
    }

    @Test
    void shouldReturnOrderStatusHistory() throws Exception {

        Order order = createOrder();

        ChangeOrderStatusRequest request =
                new ChangeOrderStatusRequest();

        request.setNewStatus(
                OrderStatus.TECHNICIAN_ACCEPTED);

        request.setComment("پذیرفته شد");

        mockMvc.perform(
                        patch("/api/orders/{uuid}/status",
                                order.getUuid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/orders/{uuid}/status-history",
                                order.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].toStatus")
                        .value("FINAL_ORDER"))
                .andExpect(jsonPath("$[1].toStatus")
                        .value("TECHNICIAN_ACCEPTED"))
                .andExpect(jsonPath("$[1].comment")
                        .value("پذیرفته شد"));
    }

    @Test
    void shouldRejectOrderWhenRequestedDateIsInPast() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest();

        request.setRequestedDate(
                LocalDate.now().minusDays(1));

        request.setCustomerUuid(customer.getUuid());
        request.setAddressUuid(address.getUuid());
        request.setServiceCategoryUuid(category.getUuid());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.requestedDate")
                        .exists());
    }

    @Test
    void shouldRejectOrderWhenAddressBelongsToAnotherCustomer() throws Exception {

        Customer anotherCustomer = new Customer();
        anotherCustomer.setFirstName("حسن");
        anotherCustomer.setLastName("محمدی");
        anotherCustomer.setMobile("09222222222");
        anotherCustomer.setNationalCode("9876543210");

        anotherCustomer =
                customerRepository.save(anotherCustomer);

        CreateOrderRequest request = new CreateOrderRequest();

        request.setRequestedDate(
                LocalDate.now().plusDays(1));

        request.setCustomerUuid(anotherCustomer.getUuid());
        request.setAddressUuid(address.getUuid());
        request.setServiceCategoryUuid(category.getUuid());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Address not found or does not belong to the customer."
                        ));
    }

    private Order createOrder() {

        Order order = new Order();

        order.setOrderCode("20260919-00001");
        order.setRequestedDate(LocalDate.now().plusDays(1));
        order.setCustomer(customer);
        order.setAddress(address);
        order.setServiceCategory(category);
        order.setStatus(OrderStatus.FINAL_ORDER);

        Order savedOrder = orderRepository.save(order);

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(savedOrder);
        history.setFromStatus(null);
        history.setToStatus(OrderStatus.FINAL_ORDER);
        history.setComment("Order created");

        historyRepository.save(history);

        return savedOrder;
    }
}