//package com.example.demo.region;
//
//import com.example.demo.dto.region.CreateRegionRequest;
//import com.example.demo.dto.region.UpdateRegionRequest;
//import com.example.demo.model.Region;
//import com.example.demo.repository.RegionRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
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
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class RegionControllerCriticalTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    @BeforeEach
//    void setUp() {
//        regionRepository.deleteAll();
//    }
//
//    private Region createAndSaveRegion(String name, boolean enabled) {
//        Region region = new Region();
//        region.setName(name);
//        region.setEnabled(enabled);
//        region.setDeleted(false);
//        return regionRepository.save(region);
//    }
//
//    // ==================== Critical Tests ====================
//
//    @Test
//    void create_shouldPersistRegionAndReturnCreated() throws Exception {
//        CreateRegionRequest request = new CreateRegionRequest();
//        request.setName("Tehran");
//
//        mockMvc.perform(post("/api/regions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.name").value("Tehran"))
//                .andExpect(jsonPath("$.enabled").value(true));
//
//        assertThat(regionRepository.findAllActive()).hasSize(1);
//    }
//
//    @Test
//    void findById_shouldReturnRegion_whenActive() throws Exception {
//        Region region = createAndSaveRegion("Isfahan", true);
//
//        mockMvc.perform(get("/api/regions/{id}", region.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(region.getId()))
//                .andExpect(jsonPath("$.name").value("Isfahan"));
//    }
//
//    @Test
//    void findById_shouldReturn404_whenNotFoundOrDeleted() throws Exception {
//        // Not found
//        mockMvc.perform(get("/api/regions/{id}", 999L))
//                .andExpect(status().isNotFound());
//
//        // Soft-deleted
//        Region region = createAndSaveRegion("Deleted", true);
//        region.setDeleted(true);
//        regionRepository.save(region);
//
//        mockMvc.perform(get("/api/regions/{id}", region.getId()))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void update_shouldModifyExistingRegion() throws Exception {
//        Region region = createAndSaveRegion("Old Name", true);
//
//        UpdateRegionRequest request = new UpdateRegionRequest();
//        request.setName("New Name");
//        request.setEnabled(false);
//
//        mockMvc.perform(put("/api/regions/{id}", region.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("New Name"))
//                .andExpect(jsonPath("$.enabled").value(false));
//
//        Region updated = regionRepository.findById(region.getId()).orElseThrow();
//        assertThat(updated.getName()).isEqualTo("New Name");
//        assertThat(updated.isEnabled()).isFalse();
//    }
//
//    @Test
//    void softDelete_shouldMarkAsDeletedAndHideFromActiveList() throws Exception {
//        Region region = createAndSaveRegion("To Delete", true);
//
//        mockMvc.perform(delete("/api/regions/{id}", region.getId()))
//                .andExpect(status().isNoContent());
//
//        Region deleted = regionRepository.findById(region.getId()).orElseThrow();
//        assertThat(deleted.isDeleted()).isTrue();
//        assertThat(regionRepository.findAllActive()).isEmpty();
//    }
//
//    @Test
//    void findAll_shouldReturnOnlyActiveRegions() throws Exception {
//        createAndSaveRegion("Active 1", true);
//        createAndSaveRegion("Active 2", true);
//
//        Region deleted = createAndSaveRegion("Deleted", true);
//        deleted.setDeleted(true);
//        regionRepository.save(deleted);
//
//        mockMvc.perform(get("/api/regions"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//    }
//}
