package com.example.demo.region;

import com.example.demo.dto.address.CreateAddressRequest;
import com.example.demo.dto.address.UpdateAddressRequest;
import com.example.demo.dto.customer.CreateCustomerRequest;
import com.example.demo.dto.region.CreateRegionRequest;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.Region;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RegionRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void shouldReturnActiveRootRegions() throws Exception {

        Region root = new Region();
        root.setName("تهران");
        root.setEnabled(true);

        root = regionRepository.save(root);

        mockMvc.perform(get("/api/regions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid")
                        .value(root.getUuid().toString()))
                .andExpect(jsonPath("$[0].name")
                        .value("تهران"));
    }

    @Test
    void shouldFindRegionByUuid() throws Exception {

        Region region = new Region();
        region.setName("تهران");
        region.setEnabled(true);

        region = regionRepository.save(region);

        mockMvc.perform(get("/api/regions/{uuid}", region.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid")
                        .value(region.getUuid().toString()))
                .andExpect(jsonPath("$.name")
                        .value("تهران"))
                .andExpect(jsonPath("$.enabled")
                        .value(true));
    }

    @Test
    void shouldCreateRegion() throws Exception {

        CreateRegionRequest request = new CreateRegionRequest();
        request.setName("تهران");
        request.setEnabled(true);

        mockMvc.perform(post("/api/admin/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.name").value("تهران"))
                .andExpect(jsonPath("$.enabled").value(true));

        assertThat(regionRepository.findAll())
                .hasSize(1);

        Region saved = regionRepository.findAll().get(0);

        assertThat(saved.getName())
                .isEqualTo("تهران");

        assertThat(saved.isEnabled())
                .isTrue();
    }

    @Test
    void shouldCreateChildRegion() throws Exception {

        Region parent = new Region();
        parent.setName("تهران");
        parent.setEnabled(true);

        parent = regionRepository.save(parent);

        CreateRegionRequest request = new CreateRegionRequest();
        request.setName("تهران شمال");
        request.setParentUuid(parent.getUuid());
        request.setEnabled(true);

        mockMvc.perform(post("/api/admin/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parentUuid")
                        .value(parent.getUuid().toString()));
    }

    @Test
    void shouldFindChildren() throws Exception {

        Region parent = new Region();
        parent.setName("تهران");
        parent.setEnabled(true);
        parent = regionRepository.save(parent);

        Region child = new Region();
        child.setName("تهران شمال");
        child.setEnabled(true);
        child.setParent(parent);
        child = regionRepository.save(child);

        mockMvc.perform(
                        get("/api/regions/{uuid}/children",
                                parent.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid")
                        .value(child.getUuid().toString()))
                .andExpect(jsonPath("$[0].name")
                        .value("تهران شمال"));
    }

    @Test
    void shouldDisableRegion() throws Exception {

        Region region = new Region();
        region.setName("تهران");
        region.setEnabled(true);
        region = regionRepository.save(region);

        mockMvc.perform(
                        patch("/api/admin/regions/{uuid}/disable",
                                region.getUuid()))
                .andExpect(status().isNoContent());

        Region updated =
                regionRepository.findById(region.getId()).orElseThrow();

        assertThat(updated.isEnabled())
                .isFalse();
    }

    @Test
    void shouldEnableRegion() throws Exception {

        Region region = new Region();
        region.setName("تهران");
        region.setEnabled(false);
        region = regionRepository.save(region);

        mockMvc.perform(
                        patch("/api/admin/regions/{uuid}/enable",
                                region.getUuid()))
                .andExpect(status().isNoContent());

        Region updated =
                regionRepository.findById(region.getId()).orElseThrow();

        assertThat(updated.isEnabled())
                .isTrue();
    }

    @Test
    void shouldRejectRegionWhenNameIsBlank() throws Exception {

        CreateRegionRequest request = new CreateRegionRequest();
        request.setName("");

        mockMvc.perform(post("/api/admin/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }
}