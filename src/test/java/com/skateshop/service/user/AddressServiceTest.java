package com.skateshop.service.user;

import com.skateshop.commons.AddressUtils;
import com.skateshop.commons.UserUtils;
import com.skateshop.domain.user.Address;
import com.skateshop.dto.request.AddressPatchRequest;
import com.skateshop.dto.request.AddressPutRequest;
import com.skateshop.dto.response.AddressPostResponse;
import com.skateshop.dto.response.CepGetResponse;
import com.skateshop.mapper.user.AddressMapper;
import com.skateshop.repository.user.AddressRepository;
import com.skateshop.service.BrasilApiService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @InjectMocks
    private AddressUtils addressUtils;

    @Spy
    private UserUtils userUtils;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private BrasilApiService brasilApiService;

    @Mock
    private AddressMapper mapper;

    @Mock
    private UserService userService;

    private List<Address> addressList;

    @BeforeEach
    void init() {
        addressList = addressUtils.getAddressList();
    }

    @Test
    @DisplayName("findAll returns a list with all address when param is null")
    @Order(1)
    void findAll_returnsAllAddress_whenSuccessful() {
        BDDMockito.when(addressRepository.findAll()).thenReturn(addressList);

        var address = addressService.findAll(null);

        Assertions.assertThat(address).isNotNull().hasSameElementsAs(addressList);
    }


    @Test
    @DisplayName("findById returns an address if exists")
    @Order(2)
    void findById_returnsAnAddress_whenExists() {
        var address = addressList.getFirst();

        BDDMockito.when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        var addressFound = addressService.findAddressByIdOrElseThrow(address.getId());

        Assertions.assertThat(addressFound).isEqualTo(address);
    }

    @Test
    @DisplayName("findById returns exception if address is not found")
    @Order(3)
    void findById_returnsNotFound_whenAddressNotFound() {
        var address = addressList.getFirst();

        BDDMockito.when(addressRepository.findById(address.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> addressService.findAddressByIdOrElseThrow(address.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save create an address")
    @Order(4)
    void save_createAnAddress_whenSuccessful() {
        var request = addressUtils.newAddress();
        var user = userUtils.getUserList().getFirst();
        var addressToSave = addressList.getFirst();
        var cep = new CepGetResponse(
                addressToSave.getCep(),
                addressToSave.getState(),
                addressToSave.getCity(),
                addressToSave.getNeighborhood(),
                addressToSave.getStreet()
        );

        var expectedResponse = AddressPostResponse.builder()
                .id(addressToSave.getId())
                .number(addressToSave.getNumber())
                .complement(addressToSave.getComplement())
                .cep(addressToSave.getCep())
                .street(addressToSave.getStreet())
                .neighborhood(addressToSave.getNeighborhood())
                .state(addressToSave.getState())
                .city(addressToSave.getCity())
                .userId(user.getId())
                .build();

        BDDMockito.when(mapper.toAddress(request)).thenReturn(addressToSave);
        BDDMockito.when(brasilApiService.findCep(request.getCep())).thenReturn(cep);
        BDDMockito.when(userService.findUserByIdOrElseThrow(user.getId())).thenReturn(user);
        BDDMockito.when(addressRepository.save(addressToSave)).thenReturn(addressToSave);
        BDDMockito.when(mapper.toAddressPostResponse(addressToSave)).thenReturn(expectedResponse);

        var addressSaved = addressService.save(request, user.getId());

        Assertions.assertThat(addressSaved).isEqualTo(expectedResponse).hasNoNullFieldsOrProperties();
    }


    @Test
    @DisplayName("delete delete an address when exists")
    @Order(5)
    void delete_deleteAnAddress_whenExists() {
        var address = addressList.getFirst();

        BDDMockito.when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        BDDMockito.doNothing().when(addressRepository).deleteById(address.getId());

        Assertions.assertThatNoException().isThrownBy(() -> addressService.delete(address));
    }

    @Test
    @DisplayName("delete returns exception if address is not found")
    @Order(6)
    void delete_returnsNotFound_whenAddressNotFound() {
        var address = addressList.getFirst();

        BDDMockito.when(addressRepository.findById(address.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> addressService.delete(address))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an address when exists")
    @Order(7)
    void update_updatesAnAddress_whenSuccessful() {
        var addressToUpdate = addressList.getFirst();

        var request = new AddressPutRequest();
        request.setNumber("99");
        request.setComplement("99B");
        request.setCep("11111111");

        var cep = new CepGetResponse(
                request.getCep(),
                addressToUpdate.getState(),
                addressToUpdate.getCity(),
                addressToUpdate.getNeighborhood(),
                addressToUpdate.getStreet()
        );

        BDDMockito.when(addressRepository.findById(addressToUpdate.getId())).thenReturn(Optional.of(addressToUpdate));
        BDDMockito.when(brasilApiService.findCep(request.getCep())).thenReturn(cep);
        BDDMockito.when(addressRepository.save(addressToUpdate)).thenReturn(addressToUpdate);

        var addressUpdated = addressService.update(request, addressToUpdate.getId());

        Assertions.assertThat(addressUpdated)
                .isEqualTo(addressToUpdate)
                .hasNoNullFieldsOrProperties();
        Assertions.assertThat(addressUpdated.getNumber()).isEqualTo(request.getNumber());
        Assertions.assertThat(addressUpdated.getComplement()).isEqualTo(request.getComplement());
        Assertions.assertThat(addressUpdated.getCep()).isEqualTo(request.getCep());
    }

    @Test
    @DisplayName("updateSelectedFields updates only provided fields when address exists")
    @Order(8)
    void updateSelectedFields_updatesProvidedFields_whenSuccessful() {
        var addressToUpdate = addressList.getFirst();

        var request = new AddressPatchRequest();
        request.setComplement("55C");
        request.setNumber("22");
        request.setCep("22222222");

        var cep = new CepGetResponse(
                request.getCep(),
                addressToUpdate.getState(),
                addressToUpdate.getCity(),
                addressToUpdate.getNeighborhood(),
                addressToUpdate.getStreet()
        );

        BDDMockito.when(addressRepository.findById(addressToUpdate.getId())).thenReturn(Optional.of(addressToUpdate));
        BDDMockito.when(brasilApiService.findCep(request.getCep())).thenReturn(cep);
        BDDMockito.when(addressRepository.save(addressToUpdate)).thenReturn(addressToUpdate);

        var addressUpdated = addressService.updateSelectedFields(request, addressToUpdate.getId());

        Assertions.assertThat(addressUpdated)
                .isEqualTo(addressToUpdate)
                .hasNoNullFieldsOrProperties();
        Assertions.assertThat(addressUpdated.getComplement()).isEqualTo(request.getComplement());
    }

    @Test
    @DisplayName("findAllAddressByUserId returns all addresses from a user")
    @Order(9)
    void findAllAddressByUserId_returnsAllAddress_whenSuccessful() {
        var user = addressList.getFirst().getUser();

        BDDMockito.when(addressRepository.findAllByUserId(user.getId())).thenReturn(addressList);

        var address = addressService.findAllAddressByUserId(user.getId());

        Assertions.assertThat(address).isNotNull().hasSameElementsAs(addressList);
    }

    @Test
    @DisplayName("findAll returns a list with a single address when param is not null")
    @Order(10)
    void findAll_returnsSingleAddress_whenIdIsNotNull() {
        var address = addressList.getFirst();

        BDDMockito.when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        var addresses = addressService.findAll(address.getId());

        Assertions.assertThat(addresses).isNotNull().containsExactly(address);
    }
}