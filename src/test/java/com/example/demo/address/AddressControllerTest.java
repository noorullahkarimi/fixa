package com.example.demo.address;

import com.example.demo.dto.address.CreateAddressRequest;
import com.example.demo.dto.address.UpdateAddressRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Customer customer;
    private Region region;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setFirstName("علی");
        customer.setLastName("رضایی");
        customer.setMobile("09123456789");
        customer.setNationalCode("1234567890");
        customer = customerRepository.save(customer);

        region = new Region();
        region.setName("تهران");
        region.setEnabled(true);
        region = regionRepository.save(region);
    }

    @Test
    void shouldCreateAddress() throws Exception {

        CreateAddressRequest request = new CreateAddressRequest();
        request.setDetails("خیابان ولیعصر پلاک ۱۰");
        request.setCustomerUuid(customer.getUuid());
        request.setRegionUuid(region.getUuid());
        request.setLatitude(35.7);
        request.setLongitude(51.4);

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.details").value("خیابان ولیعصر پلاک ۱۰"))
                .andExpect(jsonPath("$.customerUuid").value(customer.getUuid().toString()))
                .andExpect(jsonPath("$.regionUuid").value(region.getUuid().toString()))
                .andExpect(jsonPath("$.regionName").value("تهران"));

        assertThat(addressRepository.findAll())
                .hasSize(1);

        Address saved = addressRepository.findAll().get(0);

        assertThat(saved.getDetails())
                .isEqualTo("خیابان ولیعصر پلاک ۱۰");

        assertThat(saved.getCustomer().getUuid())
                .isEqualTo(customer.getUuid());

        assertThat(saved.getRegion().getUuid())
                .isEqualTo(region.getUuid());
    }

    @Test
    void shouldRejectAddressWhenDetailsIsBlank() throws Exception {

        CreateAddressRequest request = new CreateAddressRequest();
        request.setDetails("");
        request.setCustomerUuid(customer.getUuid());
        request.setRegionUuid(region.getUuid());
        request.setLatitude(35.7);
        request.setLongitude(51.4);

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldRejectAddressWhenLatitudeIsInvalid() throws Exception {

        CreateAddressRequest request = new CreateAddressRequest();
        request.setDetails("خیابان ولیعصر");
        request.setCustomerUuid(customer.getUuid());
        request.setRegionUuid(region.getUuid());
        request.setLatitude(100.0);
        request.setLongitude(51.4);

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.latitude").exists());
    }

    @Test
    void shouldFindAddressByUuid() throws Exception {

        Address address = new Address();
        address.setDetails("خیابان انقلاب");
        address.setCustomer(customer);
        address.setRegion(region);
        address.setLatitude(35.7);
        address.setLongitude(51.4);

        address = addressRepository.save(address);

        mockMvc.perform(get("/api/addresses/{uuid}", address.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid")
                        .value(address.getUuid().toString()))
                .andExpect(jsonPath("$.details")
                        .value("خیابان انقلاب"));
    }

    @Test
    void shouldFindAddressesByCustomerUuid() throws Exception {

        Address address = new Address();
        address.setDetails("خیابان آزادی");
        address.setCustomer(customer);
        address.setRegion(region);
        address.setLatitude(35.7);
        address.setLongitude(51.4);

        addressRepository.save(address);

        mockMvc.perform(
                        get("/api/addresses/by-customer/{customerUuid}",
                                customer.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerUuid")
                        .value(customer.getUuid().toString()));
    }

    @Test
    void shouldReturn404WhenAddressDoesNotExist() throws Exception {

        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/api/addresses/{uuid}", uuid))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Address not found. uuid=" + uuid));
    }

    @Test
    void shouldUpdateAddress() throws Exception {

        Address address = new Address();
        address.setDetails("خیابان آزادی");
        address.setCustomer(customer);
        address.setRegion(region);
        address.setLatitude(35.7);
        address.setLongitude(51.4);

        address = addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setDetails("خیابان آزادی پلاک ۲۰");
        request.setRegionUuid(region.getUuid());
        request.setLatitude(36.0);
        request.setLongitude(52.0);

        mockMvc.perform(put("/api/addresses/{uuid}", address.getUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.details")
                        .value("خیابان آزادی پلاک ۲۰"))
                .andExpect(jsonPath("$.latitude")
                        .value(36.0));

        Address updated =
                addressRepository.findById(address.getId()).orElseThrow();

        assertThat(updated.getDetails())
                .isEqualTo("خیابان آزادی پلاک ۲۰");

        assertThat(updated.getLatitude())
                .isEqualTo(36.0);
    }
}