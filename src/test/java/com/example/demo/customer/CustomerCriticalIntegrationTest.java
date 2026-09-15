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
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class CustomerCriticalIntegrationTest {
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
//    // 1. Bean Validation - Create
//    // =========================================================
//
//    @Nested
//    @DisplayName("Create - Bean Validation (Critical)")
//    class CreateValidation {
//
//        @Test
//        @DisplayName("firstName خالی → 400")
//        void create_blankFirstName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "1234567890"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("firstName فقط فاصله → 400")
//        void create_firstNameOnlySpaces_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "   ",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "1234567890"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("lastName خالی → 400")
//        void create_blankLastName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "",
//                      "mobile": "09121234567",
//                      "nationalCode": "1234567890"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("mobile خالی → 400")
//        void create_blankMobile_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "",
//                      "nationalCode": "1234567890"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("nationalCode خالی → 400")
//        void create_blankNationalCode_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": ""
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("firstName بیشتر از ۵۰ کاراکتر → 400")
//        void create_firstNameTooLong_shouldReturn400() throws Exception {
//            String longName = "a".repeat(51);
//            String body = String.format("""
//                    {
//                      "firstName": "%s",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "1234567890"
//                    }
//                    """, longName);
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("lastName بیشتر از ۵۰ کاراکتر → 400")
//        void create_lastNameTooLong_shouldReturn400() throws Exception {
//            String longName = "b".repeat(51);
//            String body = String.format("""
//                    {
//                      "firstName": "Ali",
//                      "lastName": "%s",
//                      "mobile": "09121234567",
//                      "nationalCode": "1234567890"
//                    }
//                    """, longName);
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("mobile بیشتر از ۱۵ کاراکتر → 400")
//        void create_mobileTooLong_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "0912123456789012",
//                      "nationalCode": "1234567890"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("nationalCode کمتر از ۱۰ رقم → 400")
//        void create_nationalCodeTooShort_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "123456789"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("nationalCode بیشتر از ۱۰ رقم → 400")
//        void create_nationalCodeTooLong_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "12345678901"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("nationalCode شامل حرف → 400")
//        void create_nationalCodeWithLetter_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "123456789a"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("همه فیلدها null → 400")
//        void create_allNull_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": null,
//                      "lastName": null,
//                      "mobile": null,
//                      "nationalCode": null
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    // =========================================================
//    // 2. Bean Validation - Update
//    // =========================================================
//
//    @Nested
//    @DisplayName("Update - Bean Validation (Critical)")
//    class UpdateValidation {
//
//        @Test
//        @DisplayName("firstName خالی در Update → 400")
//        void update_blankFirstName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "0012345678"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", existingCustomer.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("nationalCode نامعتبر در Update → 400")
//        void update_invalidNationalCode_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "12345"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", existingCustomer.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("mobile خیلی طولانی در Update → 400")
//        void update_mobileTooLong_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "091212345678901234",
//                      "nationalCode": "0012345678"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", existingCustomer.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    // =========================================================
//    // 3. Business Rules (Critical)
//    // =========================================================
//
//    @Nested
//    @DisplayName("Business Rules (Critical)")
//    class BusinessRules {
//
//        @Test
//        @DisplayName("ایجاد با nationalCode تکراری → 400")
//        void create_duplicateNationalCode_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Sara",
//                      "lastName": "Mohammadi",
//                      "mobile": "09129876543",
//                      "nationalCode": "0012345678"
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/customers")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("nationalCode already exists")));
//        }
//
//        @Test
//        @DisplayName("Update با nationalCode تکراری (متعلق به مشتری دیگر) → 400")
//        void update_duplicateNationalCode_shouldReturn400() throws Exception {
//            // مشتری دوم
//            Customer another = new Customer();
//            another.setFirstName("Reza");
//            another.setLastName("Karimi");
//            another.setMobile("09351234567");
//            another.setNationalCode("0099887766");
//            customerRepository.save(another);
//
//            String body = """
//                    {
//                      "firstName": "Ali",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "0099887766"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", existingCustomer.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("nationalCode already exists")));
//        }
//
//        @Test
//        @DisplayName("Update با همان nationalCode خودش → باید موفق باشد")
//        void update_sameNationalCode_shouldSucceed() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Ali Updated",
//                      "lastName": "Ahmadi",
//                      "mobile": "09121234567",
//                      "nationalCode": "0012345678"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", existingCustomer.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.firstName").value("Ali Updated"))
//                    .andExpect(jsonPath("$.nationalCode").value("0012345678"));
//        }
//    }
//
//    // =========================================================
//    // 4. Not Found Cases
//    // =========================================================
//
//    @Nested
//    @DisplayName("Not Found Cases (Critical)")
//    class NotFoundCases {
//
//        @Test
//        @DisplayName("GET با id ناموجود → 404")
//        void findById_notFound() throws Exception {
//            mockMvc.perform(get("/api/customers/{id}", 999999L))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", containsString("Customer not found")));
//        }
//
//        @Test
//        @DisplayName("PUT با id ناموجود → 404")
//        void update_notFound() throws Exception {
//            String body = """
//                    {
//                      "firstName": "Test",
//                      "lastName": "Test",
//                      "mobile": "09121111111",
//                      "nationalCode": "1111111111"
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/customers/{id}", 999999L)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("DELETE با id ناموجود → 404")
//        void softDelete_notFound() throws Exception {
//            mockMvc.perform(delete("/api/customers/{id}", 999999L))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("بعد از soft delete - GET باید 404 بدهد")
//        void afterSoftDelete_findById_shouldReturn404() throws Exception {
//            mockMvc.perform(delete("/api/customers/{id}", existingCustomer.getId()))
//                    .andExpect(status().isNoContent());
//
//            mockMvc.perform(get("/api/customers/{id}", existingCustomer.getId()))
//                    .andExpect(status().isNotFound());
//        }
//
//    }
//}