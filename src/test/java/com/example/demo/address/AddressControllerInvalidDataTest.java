//package com.example.demo.address;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AddressControllerInvalidDataTest {
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//
//    // -----------------------------
//    // GET /api/addresses/{id}
//    // -----------------------------
//
//    @Test
//    void shouldRejectFindAddressWithInvalidId() throws Exception {
//
//        mockMvc.perform(get("/api/addresses/{id}", -1))
//                .andExpect(status().isNotFound());
//    }
//
//
//    @Test
//    void shouldRejectFindAddressWithNonExistingId() throws Exception {
//
//        mockMvc.perform(get("/api/addresses/{id}", 999999))
//                .andExpect(status().isNotFound());
//    }
//
//
//
//    // -----------------------------
//    // GET /api/addresses/by-customer/{customerId}
//    // -----------------------------
//
//    @Test
//    void shouldRejectFindByCustomerWithInvalidCustomerId() throws Exception {
//
//        mockMvc.perform(
//                        get("/api/addresses/by-customer/{customerId}", -10)
//                )
//                .andExpect(status().isNotFound());
//    }
//
//
//
//    // -----------------------------
//    // POST /api/addresses
//    // -----------------------------
//
//    @Test
//    void shouldRejectCreateAddressWithEmptyDetails() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"",
//                    "customerId":1,
//                    "regionId":1,
//                    "latitude":52.5200,
//                    "longitude":13.4050
//                }
//                """;
//
//
//        mockMvc.perform(post("/api/addresses")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//
//    @Test
//    void shouldRejectCreateAddressWithoutCustomerId() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"Berlin street",
//                    "customerId":null,
//                    "regionId":1,
//                    "latitude":52.5200,
//                    "longitude":13.4050
//                }
//                """;
//
//
//        mockMvc.perform(post("/api/addresses")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//
//    @Test
//    void shouldRejectCreateAddressWithoutRegionId() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"Berlin street",
//                    "customerId":1,
//                    "regionId":null,
//                    "latitude":52.5200,
//                    "longitude":13.4050
//                }
//                """;
//
//
//        mockMvc.perform(post("/api/addresses")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//
//    @Test
//    void shouldRejectCreateAddressWithInvalidLatitude() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"Berlin street",
//                    "customerId":1,
//                    "regionId":1,
//                    "latitude":120,
//                    "longitude":13.4050
//                }
//                """;
//
//
//        mockMvc.perform(post("/api/addresses")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//
//    @Test
//    void shouldRejectCreateAddressWithInvalidLongitude() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"Berlin street",
//                    "customerId":1,
//                    "regionId":1,
//                    "latitude":52.5,
//                    "longitude":-300
//                }
//                """;
//
//
//        mockMvc.perform(post("/api/addresses")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//
//    // -----------------------------
//    // PUT /api/addresses/{id}
//    // -----------------------------
//
//
//    @Test
//    void shouldRejectUpdateWithInvalidId() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"Updated address",
//                    "regionId":1,
//                    "latitude":52.5,
//                    "longitude":13.4
//                }
//                """;
//
//
//        mockMvc.perform(
//                        put("/api/addresses/{id}",999999)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(body)
//                )
//                .andExpect(status().isNotFound());
//
//    }
//
//
//
//    @Test
//    void shouldRejectUpdateWithInvalidLatitude() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"Updated address",
//                    "regionId":1,
//                    "latitude":-100,
//                    "longitude":13.4
//                }
//                """;
//
//
//        mockMvc.perform(
//                        put("/api/addresses/{id}",1)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(body)
//                )
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//
//
//    @Test
//    void shouldRejectUpdateWithEmptyDetails() throws Exception {
//
//
//        String body = """
//                {
//                    "details":"",
//                    "regionId":1,
//                    "latitude":52.5,
//                    "longitude":13.4
//                }
//                """;
//
//
//        mockMvc.perform(
//                        put("/api/addresses/{id}",1)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(body)
//                )
//                .andExpect(status().isBadRequest());
//
//    }
//
//
//
//    // -----------------------------
//    // DELETE /api/addresses/{id}
//    // -----------------------------
//
//
//    @Test
//    void shouldRejectDeleteWithInvalidId() throws Exception {
//
//
//        mockMvc.perform(
//                        delete("/api/addresses/{id}",999999)
//                )
//                .andExpect(status().isNotFound());
//
//    }
//
//
//
//    @Test
//    void shouldRejectDeleteWithNegativeId() throws Exception {
//
//
//        mockMvc.perform(
//                        delete("/api/addresses/{id}",-50)
//                )
//                .andExpect(status().isNotFound());
//
//    }
//
//
//}