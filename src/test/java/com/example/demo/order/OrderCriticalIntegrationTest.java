//package com.example.demo.order;
//
//import com.example.demo.SpringWebApplication;
//import com.example.demo.model.Address;
//import com.example.demo.model.Customer;
//import com.example.demo.model.Region;
//import com.example.demo.model.ServiceCategory;
//import com.example.demo.repository.AddressRepository;
//import com.example.demo.repository.CustomerRepository;
//import com.example.demo.repository.RegionRepository;
//import com.example.demo.repository.ServiceCategoryRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.Map;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(classes = SpringWebApplication.class)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class OrderCriticalIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private ServiceCategoryRepository serviceCategoryRepository;
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    private Long customerId;
//    private Long addressId;
//    private Long serviceCategoryId;
//
//    @BeforeEach
//    void setUp() {
//        // 1. Region
//        Region region = new Region();
//        region.setName("تهران");
//        region = regionRepository.save(region);
//
//        // 2. Customer
//        Customer customer = new Customer();
//        customer.setFirstName("علی");
//        customer.setLastName("محمدی");
//        customer.setMobile("09121234567");
//        customer.setNationalCode("0012345678");
//        customer = customerRepository.save(customer);
//        this.customerId = customer.getId();
//
//        // 3. Address
//        Address address = new Address();
//        address.setDetails("تهران، خیابان ولیعصر، پلاک ۱۲۳");
//        address.setLatitude(35.6892);
//        address.setLongitude(51.3890);
//        address.setCustomer(customer);
//        address.setRegion(region);
//        address = addressRepository.save(address);
//        this.addressId = address.getId();
//
////        // 4. ServiceCategory
////        ServiceCategory category = new ServiceCategory();
////        category.setName("تعمیرات لوازم خانگی");
////        category.setEnabled(true);
////        category = serviceCategoryRepository.save(category);
////        this.serviceCategoryId = category.getId();
//
//
//        // 4. Root ServiceCategory
//        ServiceCategory rootCategory = new ServiceCategory();
//        rootCategory.setName("لوازم خانگی");
//        rootCategory.setEnabled(true);
//        rootCategory = serviceCategoryRepository.save(rootCategory);
//
//// 5. Sub ServiceCategory
//        ServiceCategory category = new ServiceCategory();
//        category.setName("تعمیرات لوازم خانگی");
//        category.setEnabled(true);
//        category.setParentId(rootCategory.getId());
//
//        category = serviceCategoryRepository.save(category);
//
//        this.serviceCategoryId = category.getId();
//    }
//
//    // ==================== CREATE ====================
//
//    @Test
//    @DisplayName("create موفق → 201 + orderCode ساخته می‌شود")
//    void create_validRequest_shouldReturn201() throws Exception {
//        String body = """
//                {
//                  "serviceCategoryId": %d,
//                  "customerId": %d,
//                  "addressId": %d,
//                  "requestedDate": "%s"
//                }
//                """.formatted(serviceCategoryId, customerId, addressId, LocalDate.now().plusDays(1));
//
//        mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.orderCode").isNotEmpty())
//                .andExpect(jsonPath("$.orderCode").value(matchesPattern("\\d{8}-\\d{5}")));
//    }
//
//    @Test
//    @DisplayName("create با فیلدهای null → 400")
//    void create_allNull_shouldReturn400() throws Exception {
//        String body = """
//                {
//                  "serviceCategoryId": null,
//                  "customerId": null,
//                  "addressId": null,
//                  "requestedDate": null
//                }
//                """;
//
//        mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("create با requestedDate گذشته → 400")
//    void create_pastRequestedDate_shouldReturn400() throws Exception {
//        String body = """
//                {
//                  "serviceCategoryId": %d,
//                  "customerId": %d,
//                  "addressId": %d,
//                  "requestedDate": "%s"
//                }
//                """.formatted(serviceCategoryId, customerId, addressId, LocalDate.now().minusDays(1));
//
//        mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("create بدون serviceCategoryId → 400")
//    void create_missingServiceCategoryId_shouldReturn400() throws Exception {
//        String body = """
//                {
//                  "customerId": %d,
//                  "addressId": %d,
//                  "requestedDate": "%s"
//                }
//                """.formatted(customerId, addressId, LocalDate.now().plusDays(1));
//
//        mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//    }
//
//    // ==================== FIND ====================
//
//    @Test
//    @DisplayName("findAll → 200 و لیست برمی‌گرداند")
//    void findAll_shouldReturn200() throws Exception {
//        mockMvc.perform(get("/api/orders"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    @DisplayName("findById موجود → 200")
//    void findById_existing_shouldReturn200() throws Exception {
//        Long orderId = createSampleOrder();
//
//        mockMvc.perform(get("/api/orders/{id}", orderId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(orderId))
//                .andExpect(jsonPath("$.orderCode").isNotEmpty())
//                .andExpect(jsonPath("$.status").value("FINAL_ORDER"))
//                .andExpect(jsonPath("$.customerId").exists())
//                .andExpect(jsonPath("$.addressId").exists())
//                .andExpect(jsonPath("$.serviceCategoryId").exists());
//    }
//
//    @Test
//    @DisplayName("findById ناموجود → 404")
//    void findById_notFound_shouldReturn404() throws Exception {
//        mockMvc.perform(get("/api/orders/{id}", 999999L))
//                .andExpect(status().isNotFound());
//    }
//
//    // ==================== CHANGE STATUS ====================
//
//    @Test
//    @DisplayName("تغییر وضعیت معتبر FINAL_ORDER → TECHNICIAN_ACCEPTED → 200")
//    void changeStatus_validTransition_shouldReturn200() throws Exception {
//        Long orderId = createSampleOrder();
//
//        String body = """
//                {
//                  "newStatus": "TECHNICIAN_ACCEPTED",
//                  "comment": "تکنسین قبول کرد"
//                }
//                """;
//
//        mockMvc.perform(patch("/api/orders/{id}/status", orderId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("TECHNICIAN_ACCEPTED"));
//    }
//
//    @Test
//    @DisplayName("تغییر وضعیت نامعتبر → 4xx")
//    void changeStatus_invalidTransition_shouldReturnError() throws Exception {
//        Long orderId = createSampleOrder();
//
//        String body = """
//                {
//                  "newStatus": "ORDER_COMPLETED",
//                  "comment": "غیرمجاز"
//                }
//                """;
//
//        mockMvc.perform(patch("/api/orders/{id}/status", orderId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    @DisplayName("changeStatus بدون newStatus → 400")
//    void changeStatus_nullNewStatus_shouldReturn400() throws Exception {
//        Long orderId = createSampleOrder();
//
//        String body = """
//                {
//                  "newStatus": null,
//                  "comment": "تست"
//                }
//                """;
//
//        mockMvc.perform(patch("/api/orders/{id}/status", orderId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//    }
//
//    // ==================== STATUS HISTORY ====================
//
//    @Test
//    @DisplayName("getStatusHistory بعد از ایجاد → حداقل یک رکورد دارد")
//    void getStatusHistory_afterCreate_shouldHaveInitialRecord() throws Exception {
//        Long orderId = createSampleOrder();
//
//        mockMvc.perform(get("/api/orders/{id}/status-history", orderId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
//                .andExpect(jsonPath("$[0].toStatus").value("FINAL_ORDER"))
//                .andExpect(jsonPath("$[0].fromStatus").value(nullValue()));
//    }
//
//    @Test
//    @DisplayName("getStatusHistory برای سفارش ناموجود → 404")
//    void getStatusHistory_notFound_shouldReturn404() throws Exception {
//        mockMvc.perform(get("/api/orders/{id}/status-history", 999999L))
//                .andExpect(status().isNotFound());
//    }
//
//    // ==================== SOFT DELETE ====================
//
//    @Test
//    @DisplayName("softDelete موفق → 204 و بعد از آن findById → 404")
//    void softDelete_thenFindById_shouldReturn404() throws Exception {
//        Long orderId = createSampleOrder();
//
//        mockMvc.perform(delete("/api/orders/{id}", orderId))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(get("/api/orders/{id}", orderId))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("softDelete سفارش ناموجود → 404")
//    void softDelete_notFound_shouldReturn404() throws Exception {
//        mockMvc.perform(delete("/api/orders/{id}", 999999L))
//                .andExpect(status().isNotFound());
//    }
//    private Long createSampleOrder() throws Exception {
//        String body = """
//            {
//              "serviceCategoryId": %d,
//              "customerId": %d,
//              "addressId": %d,
//              "requestedDate": "%s"
//            }
//            """.formatted(serviceCategoryId, customerId, addressId, LocalDate.now().plusDays(2));
//
//        System.out.println("=== createSampleOrder Request ===");
//        System.out.println(body);
//
//        var result = mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andDo(print())
//                .andReturn();
//
//        int status = result.getResponse().getStatus();
//        String responseBody = result.getResponse().getContentAsString();
//
//        System.out.println("=== Status: " + status);
//        System.out.println("=== Response Body: " + responseBody);
//
//        if (status != 201) {
//            throw new RuntimeException("createSampleOrder failed with status " + status + " → " + responseBody);
//        }
//
//        Map<?, ?> map = objectMapper.readValue(responseBody, Map.class);
//        return ((Number) map.get("id")).longValue();
//    }
//
//}