package com.example.demo.services;

import com.example.demo.dto.address.CreateAddressRequest;
import com.example.demo.dto.address.UpdateAddressRequest;
import com.example.demo.dto.customer.CreateCustomerRequest;
import com.example.demo.dto.services.CreateServiceCategoryRequest;
import com.example.demo.dto.services.UpdateServiceCategoryRequest;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.Region;
import com.example.demo.model.ServiceCategory;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.ServiceCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ServiceCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceCategoryRepository repository;

    @Test
    void shouldCreateRootCategory() throws Exception {

        CreateServiceCategoryRequest request =
                new CreateServiceCategoryRequest();

        request.setName("خدمات خودرو");
        request.setEnabled(true);
        request.setParentId(null);

        mockMvc.perform(post("/api/service-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.name")
                        .value("خدمات خودرو"))
                .andExpect(jsonPath("$.enabled")
                        .value(true))
                .andExpect(jsonPath("$.parentId").doesNotExist());

        assertThat(repository.findAll())
                .hasSize(1);

        ServiceCategory saved =
                repository.findAll().get(0);

        assertThat(saved.getName())
                .isEqualTo("خدمات خودرو");

        assertThat(saved.getParentId())
                .isNull();
    }

    @Test
    void shouldCreateChildCategory() throws Exception {

        ServiceCategory parent = new ServiceCategory();
        parent.setName("خودرو");
        parent.setEnabled(true);

        parent = repository.save(parent);

        CreateServiceCategoryRequest request =
                new CreateServiceCategoryRequest();

        request.setName("کارواش");
        request.setEnabled(true);
        request.setParentId(parent.getUuid());

        mockMvc.perform(post("/api/service-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parentId")
                        .value(parent.getUuid().toString()));

        ServiceCategory child =
                repository.findAll()
                        .stream()
                        .filter(x -> x.getName().equals("کارواش"))
                        .findFirst()
                        .orElseThrow();

        assertThat(child.getParentId())
                .isEqualTo(parent.getUuid());
    }

    @Test
    void shouldFindCategoryByUuid() throws Exception {

        ServiceCategory category = new ServiceCategory();
        category.setName("کارواش");
        category.setEnabled(true);

        ServiceCategory saved =
                repository.save(category);

        mockMvc.perform(
                        get("/api/service-categories/{uuid}",
                                saved.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid")
                        .value(saved.getUuid().toString()))
                .andExpect(jsonPath("$.name")
                        .value("کارواش"));
    }

    @Test
    void shouldReturnChildrenForRootCategory() throws Exception {

        ServiceCategory parent = new ServiceCategory();
        parent.setName("خودرو");
        parent.setEnabled(true);

        parent = repository.save(parent);

        ServiceCategory child = new ServiceCategory();
        child.setName("کارواش");
        child.setEnabled(true);
        child.setParentId(parent.getUuid());

        repository.save(child);

        mockMvc.perform(
                        get("/api/service-categories/{uuid}",
                                parent.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isArray())
                .andExpect(jsonPath("$.children[0].name")
                        .value("کارواش"));
    }

    @Test
    void shouldRejectCategoryWithThirdLevelParent() throws Exception {

        ServiceCategory root = new ServiceCategory();
        root.setName("خودرو");
        root.setEnabled(true);
        root = repository.save(root);

        ServiceCategory child = new ServiceCategory();
        child.setName("کارواش");
        child.setEnabled(true);
        child.setParentId(root.getUuid());
        child = repository.save(child);

        CreateServiceCategoryRequest request =
                new CreateServiceCategoryRequest();

        request.setName("شستشوی موتور");
        request.setEnabled(true);
        request.setParentId(child.getUuid());

        mockMvc.perform(post("/api/service-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Only two levels are supported. Cannot create a sub-category under another sub-category"));
    }

    @Test
    void shouldRejectCategoryAsItsOwnParent() throws Exception {

        ServiceCategory category = new ServiceCategory();
        category.setName("خودرو");
        category.setEnabled(true);

        category = repository.save(category);

        UpdateServiceCategoryRequest request =
                new UpdateServiceCategoryRequest();

        request.setName("خودرو");
        request.setEnabled(true);
        request.setParentId(category.getUuid());

        mockMvc.perform(
                        put("/api/service-categories/{uuid}",
                                category.getUuid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("A category cannot be its own parent"));
    }

    @Test
    void shouldRejectInvalidCategoryName() throws Exception {

        CreateServiceCategoryRequest request =
                new CreateServiceCategoryRequest();

        request.setName("");
        request.setEnabled(true);

        mockMvc.perform(post("/api/service-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }
}