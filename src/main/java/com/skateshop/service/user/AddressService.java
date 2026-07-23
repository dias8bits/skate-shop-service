package com.skateshop.service.user;

import com.skateshop.domain.user.Address;
import com.skateshop.dto.request.AddressPatchRequest;
import com.skateshop.dto.request.AddressPostRequest;
import com.skateshop.dto.request.AddressPutRequest;
import com.skateshop.dto.response.AddressPostResponse;
import com.skateshop.exception.NotFoundException;
import com.skateshop.mapper.user.AddressMapper;
import com.skateshop.repository.user.AddressRepository;
import com.skateshop.service.BrasilApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final BrasilApiService brasilApiService;
    private final AddressMapper mapper;

    public List<Address> findAll(UUID id) {
        return id == null ? addressRepository.findAll() : Collections.singletonList(findAddressByIdOrElseThrow(id));
    }

    public List<Address> findAllAddressByUserId(UUID id) {
        return addressRepository.findAllByUserId(id);
    }

    public Address findAddressByIdOrElseThrow(UUID id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("address not found"));
    }

    @Transactional
    public AddressPostResponse save(AddressPostRequest request, UUID userId) {
        var addressToSave = mapper.toAddress(request);

        var cep = brasilApiService.findCep(request.getCep());

        addressToSave.setStreet(cep.street());
        addressToSave.setNeighborhood(cep.neighborhood());
        addressToSave.setState(cep.state());
        addressToSave.setCity(cep.city());

        var user = userService.findUserByIdOrElseThrow(userId);

        addressToSave.setUser(user);

        var savedAddress = addressRepository.save(addressToSave);

        return mapper.toAddressPostResponse(savedAddress);
    }

    public void delete(Address address) {
        findAddressByIdOrElseThrow(address.getId());
        addressRepository.deleteById(address.getId());
    }

    @Transactional
    public Address update(AddressPutRequest request, UUID id) {
        var addressToUpdate = findAddressByIdOrElseThrow(id);

        var cep = brasilApiService.findCep(request.getCep());

        addressToUpdate.setNumber(request.getNumber());
        addressToUpdate.setComplement(request.getComplement());
        addressToUpdate.setCep(request.getCep());
        addressToUpdate.setStreet(cep.street());
        addressToUpdate.setNeighborhood(cep.neighborhood());
        addressToUpdate.setState(cep.state());
        addressToUpdate.setCity(cep.city());

        var savedAddress = addressRepository.save(addressToUpdate);

        return addressRepository.save(savedAddress);
    }

    public Address updateSelectedFields(AddressPatchRequest request, UUID id) {
        var addressToUpdate = findAddressByIdOrElseThrow(id);

        if (request.getCep() != null) {
            var cep = brasilApiService.findCep(request.getCep());
            addressToUpdate.setCep(request.getCep());
            addressToUpdate.setStreet(cep.street());
            addressToUpdate.setNeighborhood(cep.neighborhood());
            addressToUpdate.setState(cep.state());
            addressToUpdate.setCity(cep.city());
        }

        if (request.getComplement() != null)
            addressToUpdate.setComplement(request.getComplement());

        if (request.getNumber() != null)
            addressToUpdate.setNumber(request.getNumber());

        return addressRepository.save(addressToUpdate);
    }

}
