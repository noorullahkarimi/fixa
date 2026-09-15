//package com.example.demo.address;
//
//import com.example.demo.dto.address.AddressResponse;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//import com.example.demo.dto.address.CreateAddressRequest;
//import com.example.demo.model.Address;
//import com.example.demo.model.Customer;
//import com.example.demo.model.Region;
//import com.example.demo.repository.AddressRepository;
//import com.example.demo.service.AddressService;
//import com.example.demo.service.CustomerService;
//import com.example.demo.service.RegionService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//@SpringBootTest
//class AddressServiceTest {
//
//
//    @MockBean
//    private AddressRepository addressRepository;
//
//    @MockBean
//    private CustomerService customerService;
//
//    @MockBean
//    private RegionService regionService;
//
//
//    @Autowired
//    private AddressService addressService;
//
//
//    @Test
//    void shouldCreateAddressSuccessfully() {
//
//
//        CreateAddressRequest request = new CreateAddressRequest();
//
//        request.setDetails("Berlin Street");
//        request.setCustomerId(1L);
//        request.setRegionId(10L);
//        request.setLatitude(52.5200);
//        request.setLongitude(13.4050);
//
//
//
//        Customer customer = new Customer();
//        customer.setId(1L);
//
//
//        Region region = new Region();
//        region.setId(10L);
//        region.setName("Berlin");
//
//
//
//        Address savedAddress = new Address();
//
//        savedAddress.setId(100L);
//        savedAddress.setDetails("Berlin Street");
//        savedAddress.setCustomer(customer);
//        savedAddress.setRegion(region);
//        savedAddress.setLatitude(52.5200);
//        savedAddress.setLongitude(13.4050);
//
//
//
//        when(customerService.getActiveEntity(1L))
//                .thenReturn(customer);
//
//
//        when(regionService.getEnabledAndActive(10L))
//                .thenReturn(region);
//
//
//
//        when(addressRepository.save(any(Address.class)))
//                .thenReturn(savedAddress);
//
//
//
//        AddressResponse response =
//                addressService.create(request);
//
//
//
//        assertThat(response)
//                .isNotNull();
//
//
//
//        assertThat(response.getId())
//                .isEqualTo(100L);
//
//
//
//        assertThat(response.getDetails())
//                .isEqualTo("Berlin Street");
//
//
//
//        assertThat(response.getCustomerId())
//                .isEqualTo(1L);
//
//
//
//        assertThat(response.getRegionId())
//                .isEqualTo(10L);
//
//
//
//        assertThat(response.getLatitude())
//                .isEqualTo(52.5200);
//
//
//
//        verify(addressRepository)
//                .save(any(Address.class));
//
//    }
//
//
//
//}