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
//class RegionControllerIntegrationTest {
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
//    // ---------- Helper methods ----------
//
//    private Region createAndSaveRegion(String name, boolean enabled) {
//        Region region = new Region();
//        region.setName(name);
//        region.setEnabled(enabled);
//        region.setDeleted(false);
//        return regionRepository.save(region);
//    }
//
//    private CreateRegionRequest createRequest(String name) {
//        CreateRegionRequest request = new CreateRegionRequest();
//        request.setName(name);
//        return request;
//    }
//
//    private UpdateRegionRequest updateRequest(String name, Boolean enabled) {
//        UpdateRegionRequest request = new UpdateRegionRequest();
//        request.setName(name);
//        request.setEnabled(enabled);
//        return request;
//    }
//
//    // ---------- Tests ----------
//
//    @Test
//    void findAll_shouldReturnOnlyActiveRegions() throws Exception {
//        createAndSaveRegion("Tehran", true);
//        createAndSaveRegion("Isfahan", true);
//
//        Region deleted = createAndSaveRegion("Deleted Region", true);
//        deleted.setDeleted(true);
//        regionRepository.save(deleted);
//
//        mockMvc.perform(get("/api/regions")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name").value("Tehran"))
//                .andExpect(jsonPath("$[1].name").value("Isfahan"));
//    }
//
//    @Test
//    void findById_shouldReturnRegion_whenExistsAndActive() throws Exception {
//        Region region = createAndSaveRegion("Shiraz", true);
//
//        mockMvc.perform(get("/api/regions/{id}", region.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(region.getId()))
//                .andExpect(jsonPath("$.name").value("Shiraz"))
//                .andExpect(jsonPath("$.enabled").value(true));
//    }
//
//    @Test
//    void findById_shouldReturn404_whenRegionNotFound() throws Exception {
//        mockMvc.perform(get("/api/regions/{id}", 999L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void findById_shouldReturn404_whenRegionIsDeleted() throws Exception {
//        Region region = createAndSaveRegion("Deleted", true);
//        region.setDeleted(true);
//        regionRepository.save(region);
//
//        mockMvc.perform(get("/api/regions/{id}", region.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void create_shouldPersistAndReturnRegion() throws Exception {
//        CreateRegionRequest request = createRequest("Mashhad");
//
//        mockMvc.perform(post("/api/regions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.name").value("Mashhad"))
//                .andExpect(jsonPath("$.enabled").value(true))
//                .andExpect(jsonPath("$.deleted").doesNotExist()); // معمولاً در Response نمایش داده نمی‌شود
//
//        assertThat(regionRepository.findAllActive()).hasSize(1);
//        assertThat(regionRepository.findAllActive().get(0).getName()).isEqualTo("Mashhad");
//    }
//
//    @Test
//    void create_shouldReturn400_whenNameIsBlank() throws Exception {
//        CreateRegionRequest request = createRequest("   ");
//
//        mockMvc.perform(post("/api/regions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void update_shouldUpdateExistingRegion() throws Exception {
//        Region region = createAndSaveRegion("Old Name", true);
//
//        UpdateRegionRequest request = updateRequest("New Name", false);
//
//        mockMvc.perform(put("/api/regions/{id}", region.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(region.getId()))
//                .andExpect(jsonPath("$.name").value("New Name"))
//                .andExpect(jsonPath("$.enabled").value(false));
//
//        Region updated = regionRepository.findById(region.getId()).orElseThrow();
//        assertThat(updated.getName()).isEqualTo("New Name");
//        assertThat(updated.isEnabled()).isFalse();
//        assertThat(updated.isDeleted()).isFalse();
//    }
//
//    @Test
//    void update_shouldReturn404_whenRegionNotFound() throws Exception {
//        UpdateRegionRequest request = updateRequest("Whatever", true);
//
//        mockMvc.perform(put("/api/regions/{id}", 999L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void softDelete_shouldMarkAsDeleted() throws Exception {
//        Region region = createAndSaveRegion("To Delete", true);
//
//        mockMvc.perform(delete("/api/regions/{id}", region.getId()))
//                .andExpect(status().isNoContent());
//
//        Region deleted = regionRepository.findById(region.getId()).orElseThrow();
//        assertThat(deleted.isDeleted()).isTrue();
//
//        // نباید در findAllActive دیده شود
//        assertThat(regionRepository.findAllActive()).isEmpty();
//    }
//
//    @Test
//    void softDelete_shouldReturn404_whenRegionNotFound() throws Exception {
//        mockMvc.perform(delete("/api/regions/{id}", 999L))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void softDelete_shouldReturn404_whenAlreadyDeleted() throws Exception {
//        Region region = createAndSaveRegion("Already Deleted", true);
//        region.setDeleted(true);
//        regionRepository.save(region);
//
//        mockMvc.perform(delete("/api/regions/{id}", region.getId()))
//                .andExpect(status().isNotFound());
//    }
//}
