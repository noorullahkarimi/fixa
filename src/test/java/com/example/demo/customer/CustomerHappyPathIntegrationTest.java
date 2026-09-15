//package com.example.demo.customer;
//
//import com.example.demo.model.Customer;
//import com.example.demo.repository.CustomerRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class CustomerHappyPathIntegrationTest {
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
//    private Customer existingCustomer;
//
//    @BeforeEach
//    void setUp() {
//        customerRepository.deleteAll();
//
//        existingCustomer = new Customer();
//        existingCustomer.setFirstName("Ali");
//        existingCustomer.setLastName("Ahmadi");
//        existingCustomer.setMobile("09121234567");
//        existingCustomer.setNationalCode("0012345678");
//        existingCustomer.setDeleted(false);
//        existingCustomer = customerRepository.save(existingCustomer);
//    }
//
//    // =========================================================
//    // Happy Path Tests
//    // =========================================================
//
//    @Nested
//    @DisplayName("Happy Path - Customer")
//    class HappyPath {
//
//        @Test
//        @DisplayName("POST /api/customers - ایجاد مشتری موفق")
//        void create_success() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Sara",
//                      "lastName": "Mohammadi",
//                      "mobile": "09129876543",
//                      "nationalCode": "0011223344"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.id").exists())
//                    .andExpect(jsonPath("$.uuid").exists())
//                    .andExpect(jsonPath("$.firstName").value("Sara"))
//                    .andExpect(jsonPath("$.lastName").value("Mohammadi"))
//                    .andExpect(jsonPath("$.mobile").value("09129876543"))
//                    .andExpect(jsonPath("$.nationalCode").value("0011223344"))
//                    .andExpect(jsonPath("$.createdAt").exists())
//                    .andExpect(jsonPath("$.updatedAt").exists());
//        }
//
//        @Test
//        @DisplayName("GET /api/customers - لیست همه مشتریان فعال")
//        void findAll_success() throws Exception {
//            // یک مشتری دیگر هم اضافه می‌کنیم
//            Customer another = new Customer();
//            another.setFirstName("Reza");
//            another.setLastName("Karimi");
//            another.setMobile("09351234567");
//            another.setNationalCode("0099887766");
//            customerRepository.save(another);
//
//            mockMvc.perform(get("/api/customers"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", hasSize(2)))
//                    .andExpect(jsonPath("$[*].firstName", hasItems("Ali", "Reza")))
//                    .andExpect(jsonPath("$[*].nationalCode", hasItems("0012345678", "0099887766")));
//        }
//
//        @Test
//        @DisplayName("GET /api/customers/{id} - پیدا کردن مشتری با id")
//        void findById_success() throws Exception {
//            mockMvc.perform(get("/api/customers/{id}", existingCustomer.getId()))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(existingCustomer.getId()))
//                    .andExpect(jsonPath("$.uuid").value(existingCustomer.getUuid().toString()))
//                    .andExpect(jsonPath("$.firstName").value("Ali"))
//                    .andExpect(jsonPath("$.lastName").value("Ahmadi"))
//                    .andExpect(jsonPath("$.mobile").value("09121234567"))
//                    .andExpect(jsonPath("$.nationalCode").value("0012345678"));
//        }
//
//        @Test
//        @DisplayName("PUT /api/customers/{id} - آپدیت موفق")
//        void update_success() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali Updated",
//                      "lastName": "Ahmadi Updated",
//                      "mobile": "09120001122",
//                      "nationalCode": "0012345678"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", existingCustomer.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(existingCustomer.getId()))
//                    .andExpect(jsonPath("$.firstName").value("Ali Updated"))
//                    .andExpect(jsonPath("$.lastName").value("Ahmadi Updated"))
//                    .andExpect(jsonPath("$.mobile").value("09120001122"))
//                    .andExpect(jsonPath("$.nationalCode").value("0012345678"));
//        }
//
//        @Test
//        @DisplayName("PUT - تغییر nationalCode به مقدار جدید (که تکراری نیست) موفق")
//        void update_changeNationalCode_success() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "1122334455"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", existingCustomer.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.nationalCode").value("1122334455"));
//        }
//
////        @Test
////        @DisplayName("DELETE /api/customers/{id} - soft delete موفق")
////        void softDelete_success() throws Exception {
////            mockMvc.perform(delete("/api/customers/{id}", existingCustomer.getId()))
////                    .andExpect(status().isNoContent());
////
////            // چک می‌کنیم که دیگر در findAllActive نباشد
////            assertThat(customerRepository.findByIdAndNotDeleted(existingCustomer.getId())).isEmpty();
////            assertThat(customerRepository.findAllActive().
////        }
//
//        @Test
//        @DisplayName("بعد از soft delete - GET باید 404 بدهد")
//        void afterSoftDelete_findById_shouldReturn404() throws Exception {
//            // اول soft delete
//            mockMvc.perform(delete("/api/customers/{id}", existingCustomer.getId()))
//                    .andExpect(status().isNoContent());
//
//            // بعد GET
//            mockMvc.perform(get("/api/customers/{id}", existingCustomer.getId()))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("ایجاد مشتری با تمام فیلدهای معتبر (مرزها)")
//        void create_withMaxLengthFields_success() throws Exception {
//            String body = """
//                    {
//                      "firstName": "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx",
//                      "lastName": "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx",
//                      "mobile": "091234567890123",
//                      "nationalCode": "1234567890"
//                    }
//                    """;
//            // firstName و lastName = ۵۰ کاراکتر، mobile = ۱۵ کاراکتر، nationalCode = ۱۰ رقم
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.firstName").value("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx"))
//                    .andExpect(jsonPath("$.mobile").value("091234567890123"))
//                    .andExpect(jsonPath("$.nationalCode").value("1234567890"));
//        }
//    }
//}