//package com.example.demo.services;
//
//import com.example.demo.model.ServiceCategory;
//import com.example.demo.repository.ServiceCategoryRepository;
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
//class ServiceCategoryValidationIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private ServiceCategoryRepository repository;
//
//    private ServiceCategory rootCategory;
//    private ServiceCategory childCategory;
//
//    @BeforeEach
//    void setUp() {
//        repository.deleteAll();
//
//        rootCategory = new ServiceCategory();
//        rootCategory.setName("Root Category");
//        rootCategory.setEnabled(true);
//        rootCategory.setDeleted(false);
//        rootCategory = repository.save(rootCategory);
//
//        childCategory = new ServiceCategory();
//        childCategory.setName("Child Category");
//        childCategory.setEnabled(true);
//        childCategory.setDeleted(false);
//        childCategory.setParentId(rootCategory.getId());
//        childCategory = repository.save(childCategory);
//    }
//
//    // =========================================================
//    // 1. Validation Annotation Tests (Create)
//    // =========================================================
//
//    @Nested
//    @DisplayName("Create - Bean Validation Tests")
//    class CreateValidationTests {
//
//        @Test
//        @DisplayName("name خالی → 400")
//        void create_blankName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "name": "",
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("name فقط فاصله → 400")
//        void create_nameOnlySpaces_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "name": "     ",
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("name = null → 400")
//        void create_nullName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "name": null,
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("بدون فیلد name → 400")
//        void create_missingName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("name بیشتر از ۱۰۰ کاراکتر → 400")
//        void create_nameLongerThan100_shouldReturn400() throws Exception {
//            String longName = "a".repeat(101);
//            String body = String.format("""
//                    {
//                      "name": "%s",
//                      "enabled": true
//                    }
//                    """, longName);
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("name دقیقاً ۱۰۰ کاراکتر → باید موفق باشد (مرز)")
//        void create_nameExactly100_shouldSucceed() throws Exception {
//            String exact100 = "a".repeat(100);
//            String body = String.format("""
//                    {
//                      "name": "%s",
//                      "enabled": true
//                    }
//                    """, exact100);
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.name").value(exact100));
//        }
//    }
//
//    // =========================================================
//    // 2. Validation Annotation Tests (Update)
//    // =========================================================
//
//    @Nested
//    @DisplayName("Update - Bean Validation Tests")
//    class UpdateValidationTests {
//
//        @Test
//        @DisplayName("name خالی در Update → 400")
//        void update_blankName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "name": "",
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("name فقط فاصله در Update → 400")
//        void update_nameOnlySpaces_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "name": "   ",
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("name بیشتر از ۱۰۰ کاراکتر در Update → 400")
//        void update_nameLongerThan100_shouldReturn400() throws Exception {
//            String longName = "b".repeat(101);
//            String body = String.format("""
//                    {
//                      "name": "%s",
//                      "enabled": true
//                    }
//                    """, longName);
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("name = null در Update → 400")
//        void update_nullName_shouldReturn400() throws Exception {
//            String body = """
//                    {
//                      "name": null,
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    // =========================================================
//    // 3. Business Rule Validation Tests
//    // =========================================================
//
//    @Nested
//    @DisplayName("Business Rule Validation Tests")
//    class BusinessRuleTests {
//
//        @Test
//        @DisplayName("parentId ناموجود → 404")
//        void create_nonExistentParent_shouldReturn404() throws Exception {
//            String body = """
//                    {
//                      "name": "Invalid Parent",
//                      "enabled": true,
//                      "parentId": 999999
//                    }
//                    """;
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", containsString("Parent ServiceCategory not found")));
//        }
//
//        @Test
//        @DisplayName("ایجاد سطح سوم (child زیر child) → 400")
//        void create_thirdLevel_shouldReturn400() throws Exception {
//            String body = String.format("""
//                    {
//                      "name": "Grand Child",
//                      "enabled": true,
//                      "parentId": %d
//                    }
//                    """, childCategory.getId());
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("Only two levels are supported")));
//        }
//
//        @Test
//        @DisplayName("Update - خودش را parent کند → 400")
//        void update_selfAsParent_shouldReturn400() throws Exception {
//            String body = String.format("""
//                    {
//                      "name": "Self Parent",
//                      "enabled": true,
//                      "parentId": %d
//                    }
//                    """, rootCategory.getId());
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("cannot be its own parent")));
//        }
//
//        @Test
//        @DisplayName("Update با parentId ناموجود → 404")
//        void update_nonExistentParent_shouldReturn404() throws Exception {
//            String body = """
//                    {
//                      "name": "Bad Parent Update",
//                      "enabled": true,
//                      "parentId": 888888
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/service-categories/{id}", childCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", containsString("Parent ServiceCategory not found")));
//        }
//
//        @Test
//        @DisplayName("ایجاد با parent حذف‌شده (soft deleted) → 404")
//        void create_withDeletedParent_shouldReturn404() throws Exception {
//            // اول root را soft delete می‌کنیم
//            rootCategory.setDeleted(true);
//            repository.save(rootCategory);
//
//            String body = String.format("""
//                    {
//                      "name": "Orphan Child",
//                      "enabled": true,
//                      "parentId": %d
//                    }
//                    """, rootCategory.getId());
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    // =========================================================
//    // 4. Not Found Cases
//    // =========================================================
//
//    @Nested
//    @DisplayName("Not Found Cases")
//    class NotFoundTests {
//
//        @Test
//        @DisplayName("GET با id ناموجود → 404")
//        void findById_notFound() throws Exception {
//            mockMvc.perform(get("/api/service-categories/{id}", 999999L))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", containsString("ServiceCategory not found")));
//        }
//
//        @Test
//        @DisplayName("PUT با id ناموجود → 404")
//        void update_notFound() throws Exception {
//            String body = """
//                    {
//                      "name": "Whatever",
//                      "enabled": true
//                    }
//                    """;
//
//            mockMvc.perform(put("/api/service-categories/{id}", 999999L)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("DELETE با id ناموجود → 404")
//        void softDelete_notFound() throws Exception {
//            mockMvc.perform(delete("/api/service-categories/{id}", 999999L))
//                    .andExpect(status().isNotFound());
//        }
//    }
//}