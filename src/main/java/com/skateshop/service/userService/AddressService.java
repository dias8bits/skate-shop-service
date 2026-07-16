package com.skateshop.service.userService;


import com.skateshop.domain.user.Address;
import com.skateshop.dto.request.AddressPostRequest;
import com.skateshop.dto.response.AddressPostResponse;
import com.skateshop.exception.NotFoundException;
import com.skateshop.mapper.AddressMapper;
import com.skateshop.repository.userRepository.AddressRepository;
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

    public void delete (Address address) {
        addressRepository.deleteById(address.getId());
    }

    public Address update (Address address) {
        findAddressByIdOrElseThrow(address.getId());
        return addressRepository.save(address);
    }

    public Address updateSelectedFields (Address address) {
        var addressToUpdate = findAddressByIdOrElseThrow(address.getId());

        if (address.getCep() != null) 
            addressToUpdate.setCep(address.getCep());
        
        if (address.getComplement() != null) 
            addressToUpdate.setComplement(address.getComplement());
        
        if (address.getNumber() != null)
            addressToUpdate.setNumber(address.getNumber());

        addressToUpdate.setId(address.getId());

        return addressRepository.save(addressToUpdate);
    }

}
