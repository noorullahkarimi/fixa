package com.example.demo.customer;

import com.example.demo.dto.address.CreateAddressRequest;
import com.example.demo.dto.address.UpdateAddressRequest;
import com.example.demo.dto.customer.CreateCustomerRequest;
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
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldCreateCustomer() throws Exception {

        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFirstName("علی");
        request.setLastName("رضایی");
        request.setMobile("09123456789");
        request.setNationalCode("1234567890");

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("علی"))
                .andExpect(jsonPath("$.lastName").value("رضایی"))
                .andExpect(jsonPath("$.mobile").value("09123456789"));

        Customer saved = customerRepository
                .findByMobile("09123456789");

        assertThat(saved.getFirstName())
                .isEqualTo("علی");

        assertThat(saved.getLastName())
                .isEqualTo("رضایی");

        assertThat(saved.getUuid())
                .isNotNull();
    }

    @Test
    void shouldRejectInvalidMobile() throws Exception {

        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFirstName("علی");
        request.setLastName("رضایی");
        request.setMobile("123");
        request.setNationalCode("1234567890");

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mobile").exists());
    }

    @Test
    void shouldRejectInvalidNationalCode() throws Exception {

        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFirstName("علی");
        request.setLastName("رضایی");
        request.setMobile("09123456789");
        request.setNationalCode("123");

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nationalCode").exists());
    }

    @Test
    void shouldRejectDuplicateNationalCode() throws Exception {

        Customer customer = new Customer();
        customer.setFirstName("علی");
        customer.setLastName("رضایی");
        customer.setMobile("09111111111");
        customer.setNationalCode("1234567890");

        customerRepository.save(customer);

        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFirstName("حسن");
        request.setLastName("محمدی");
        request.setMobile("09222222222");
        request.setNationalCode("1234567890");

        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Customer with this nationalCode already exists"));
    }

    @Test
    void shouldFindCustomerByUuid() throws Exception {

        Customer customer = new Customer();
        customer.setFirstName("علی");
        customer.setLastName("رضایی");
        customer.setMobile("09123456789");
        customer.setNationalCode("1234567890");

        customer = customerRepository.save(customer);

        mockMvc.perform(get("/api/customer/{uuid}", customer.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid")
                        .value(customer.getUuid().toString()))
                .andExpect(jsonPath("$.firstName")
                        .value("علی"));
    }
}