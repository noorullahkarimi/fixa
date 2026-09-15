//package com.example.demo.services;
//
//import com.example.demo.dto.services.CreateServiceCategoryRequest;
//import com.example.demo.dto.services.UpdateServiceCategoryRequest;
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
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class ServiceCategoryIntegrationTest {
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
//        // Root فعال
//        rootCategory = new ServiceCategory();
//        rootCategory.setName("Root Category");
//        rootCategory.setEnabled(true);
//        rootCategory.setDeleted(false);
//        rootCategory = repository.save(rootCategory);
//
//        // Child فعال
//        childCategory = new ServiceCategory();
//        childCategory.setName("Child Category");
//        childCategory.setEnabled(true);
//        childCategory.setDeleted(false);
//        childCategory.setParentId(rootCategory.getId());
//        childCategory = repository.save(childCategory);
//    }
//
//    @Nested
//    @DisplayName("Happy Path Tests")
//    class HappyPath {
//
//        @Test
//        @DisplayName("GET /api/service-categories - باید لیست ریشه‌ها با فرزندان را برگرداند")
//        void findAll_shouldReturnRootsWithChildren() throws Exception {
//            mockMvc.perform(get("/api/service-categories"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", hasSize(1)))
//                    .andExpect(jsonPath("$[0].id").value(rootCategory.getId()))
//                    .andExpect(jsonPath("$[0].name").value("Root Category"))
//                    .andExpect(jsonPath("$[0].children", hasSize(1)))
//                    .andExpect(jsonPath("$[0].children[0].id").value(childCategory.getId()))
//                    .andExpect(jsonPath("$[0].children[0].name").value("Child Category"));
//        }
//
//        @Test
//        @DisplayName("GET /api/service-categories/{id} - پیدا کردن root با children")
//        void findById_root_shouldReturnWithChildren() throws Exception {
//            mockMvc.perform(get("/api/service-categories/{id}", rootCategory.getId()))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(rootCategory.getId()))
//                    .andExpect(jsonPath("$.children", hasSize(1)))
//                    .andExpect(jsonPath("$.children[0].id").value(childCategory.getId()));
//        }
//
//        @Test
//        @DisplayName("GET /api/service-categories/{id} - پیدا کردن child بدون children")
//        void findById_child_shouldReturnWithoutChildren() throws Exception {
//            mockMvc.perform(get("/api/service-categories/{id}", childCategory.getId()))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(childCategory.getId()))
//                    .andExpect(jsonPath("$.parentId").value(rootCategory.getId()))
//                    .andExpect(jsonPath("$.children").isEmpty());
//        }
//
//        @Test
//        @DisplayName("POST /api/service-categories - ایجاد root موفق")
//        void create_root_success() throws Exception {
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName("New Root");
//            request.setEnabled(true);
//            request.setParentId(null);
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.id").exists())
//                    .andExpect(jsonPath("$.name").value("New Root"))
//                    .andExpect(jsonPath("$.parentId").doesNotExist())
//                    .andExpect(jsonPath("$.enabled").value(true));
//        }
//
//        @Test
//        @DisplayName("POST /api/service-categories - ایجاد child موفق")
//        void create_child_success() throws Exception {
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName("New Child");
//            request.setEnabled(true);
//            request.setParentId(rootCategory.getId());
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.name").value("New Child"))
//                    .andExpect(jsonPath("$.parentId").value(rootCategory.getId()));
//        }
//
//        @Test
//        @DisplayName("PUT /api/service-categories/{id} - آپدیت موفق")
//        void update_success() throws Exception {
//            UpdateServiceCategoryRequest request = new UpdateServiceCategoryRequest();
//            request.setName("Updated Name");
//            request.setEnabled(false);
//            request.setParentId(null); // root می‌ماند
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.name").value("Updated Name"))
//                    .andExpect(jsonPath("$.enabled").value(false));
//        }
//
//        @Test
//        @DisplayName("DELETE /api/service-categories/{id} - soft delete root و فرزندان")
//        void softDelete_root_shouldCascadeToChildren() throws Exception {
//            mockMvc.perform(delete("/api/service-categories/{id}", rootCategory.getId()))
//                    .andExpect(status().isNoContent());
//
//            // چک دیتابیس
//            assertThat(repository.findByIdAndNotDeleted(rootCategory.getId())).isEmpty();
//            assertThat(repository.findByIdAndNotDeleted(childCategory.getId())).isEmpty();
//        }
//    }
//
//    // =========================================================
//    // Validation & Invalid Input Tests (Critical)
//    // =========================================================
//
//    @Nested
//    @DisplayName("Validation & Invalid Input Tests")
//    class ValidationAndInvalidInput {
//
//        @Test
//        @DisplayName("POST - name خالی → 400")
//        void create_blankName_shouldReturn400() throws Exception {
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName("   ");
//            request.setEnabled(true);
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("name is required"))); // بسته به GlobalExceptionHandler
//        }
//
//        @Test
//        @DisplayName("POST - name null → 400")
//        void create_nullName_shouldReturn400() throws Exception {
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName(null);
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("POST - name بیش از ۱۰۰ کاراکتر → 400")
//        void create_nameTooLong_shouldReturn400() throws Exception {
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName("a".repeat(101));
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("name must not exceed 100 characters")));
//        }
//
//        @Test
//        @DisplayName("PUT - name خالی → 400")
//        void update_blankName_shouldReturn400() throws Exception {
//            UpdateServiceCategoryRequest request = new UpdateServiceCategoryRequest();
//            request.setName("");
//            request.setEnabled(true);
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("POST - parentId ناموجود → 404")
//        void create_nonExistentParent_shouldReturn404() throws Exception {
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName("Invalid Parent");
//            request.setParentId(99999L);
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", containsString("Parent ServiceCategory not found")));
//        }
//
//        @Test
//        @DisplayName("POST - ایجاد سطح سوم (child زیر child) → 400")
//        void create_thirdLevel_shouldReturn400() throws Exception {
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName("Grand Child");
//            request.setParentId(childCategory.getId()); // child خودش parentId دارد
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("Only two levels are supported")));
//        }
//
//        @Test
//        @DisplayName("PUT - تنظیم parent به خودش → 400")
//        void update_selfAsParent_shouldReturn400() throws Exception {
//            UpdateServiceCategoryRequest request = new UpdateServiceCategoryRequest();
//            request.setName("Self Parent");
//            request.setEnabled(true);
//            request.setParentId(rootCategory.getId()); // خودش
//
//            mockMvc.perform(put("/api/service-categories/{id}", rootCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message", containsString("cannot be its own parent")));
//        }
//
//        @Test
//        @DisplayName("PUT - parentId ناموجود → 404")
//        void update_nonExistentParent_shouldReturn404() throws Exception {
//            UpdateServiceCategoryRequest request = new UpdateServiceCategoryRequest();
//            request.setName("Bad Parent");
//            request.setEnabled(true);
//            request.setParentId(88888L);
//
//            mockMvc.perform(put("/api/service-categories/{id}", childCategory.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", containsString("Parent ServiceCategory not found")));
//        }
//    }
//
//    // =========================================================
//    // Not Found & Soft-Deleted Cases
//    // =========================================================
//
//    @Nested
//    @DisplayName("Not Found & Soft Delete Cases")
//    class NotFoundAndSoftDelete {
//
//        @Test
//        @DisplayName("GET - id ناموجود → 404")
//        void findById_notFound() throws Exception {
//            mockMvc.perform(get("/api/service-categories/{id}", 99999L))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", containsString("ServiceCategory not found")));
//        }
//
//        @Test
//        @DisplayName("PUT - id ناموجود → 404")
//        void update_notFound() throws Exception {
//            UpdateServiceCategoryRequest request = new UpdateServiceCategoryRequest();
//            request.setName("Whatever");
//            request.setEnabled(true);
//
//            mockMvc.perform(put("/api/service-categories/{id}", 99999L)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("DELETE - id ناموجود → 404")
//        void softDelete_notFound() throws Exception {
//            mockMvc.perform(delete("/api/service-categories/{id}", 99999L))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("بعد از soft delete - GET باید 404 بدهد")
//        void afterSoftDelete_findById_shouldReturn404() throws Exception {
//            // اول soft delete
//            mockMvc.perform(delete("/api/service-categories/{id}", childCategory.getId()))
//                    .andExpect(status().isNoContent());
//
//            // بعد GET
//            mockMvc.perform(get("/api/service-categories/{id}", childCategory.getId()))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("soft delete روی child - فقط خودش حذف شود")
//        void softDelete_child_onlyItself() throws Exception {
//            mockMvc.perform(delete("/api/service-categories/{id}", childCategory.getId()))
//                    .andExpect(status().isNoContent());
//
//            assertThat(repository.findByIdAndNotDeleted(childCategory.getId())).isEmpty();
//            assertThat(repository.findByIdAndNotDeleted(rootCategory.getId())).isPresent();
//        }
//    }
//
//    // =========================================================
//    // Edge Cases اضافی (پیشنهادی)
//    // =========================================================
//
//    @Nested
//    @DisplayName("Additional Edge Cases")
//    class EdgeCases {
//
//        @Test
//        @DisplayName("ایجاد با parent حذف‌شده → 404")
//        void create_withDeletedParent_shouldReturn404() throws Exception {
//            // اول parent را soft delete کن
//            rootCategory.setDeleted(true);
//            repository.save(rootCategory);
//
//            CreateServiceCategoryRequest request = new CreateServiceCategoryRequest();
//            request.setName("Orphan Child");
//            request.setParentId(rootCategory.getId());
//
//            mockMvc.perform(post("/api/service-categories")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("findAll بعد از soft delete root - نباید چیزی برگرداند")
//        void findAll_afterRootSoftDelete_shouldBeEmpty() throws Exception {
//            mockMvc.perform(delete("/api/service-categories/{id}", rootCategory.getId()))
//                    .andExpect(status().isNoContent());
//
//            mockMvc.perform(get("/api/service-categories"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", hasSize(0)));
//        }
//    }
//}