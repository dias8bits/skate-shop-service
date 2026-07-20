package com.skateshop.controller.user;

import com.skateshop.dto.request.AddressPatchRequest;
import com.skateshop.dto.request.AddressPostRequest;
import com.skateshop.dto.request.AddressPutRequest;
import com.skateshop.dto.response.AddressGetResponse;
import com.skateshop.dto.response.AddressPatchResponse;
import com.skateshop.dto.response.AddressPostResponse;
import com.skateshop.dto.response.AddressPutResponse;
import com.skateshop.mapper.user.AddressMapper;
import com.skateshop.service.user.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/address")
@RequiredArgsConstructor
@Log4j2
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper mapper;

    @GetMapping
    public ResponseEntity<List<AddressGetResponse>> findAll(@RequestParam(required = false) UUID id) {
        log.debug("request to find all address ");

        var address = addressService.findAll(id);

        var addressGetResponseList = mapper.toAddressGetResponseList(address);

        return ResponseEntity.ok().body(addressGetResponseList);
    }


    @GetMapping("user/{userId}")
    public ResponseEntity<List<AddressGetResponse>> findAllAddressByUserId(@PathVariable UUID userId) {
        log.debug("request to find all address by user id: '{}'", userId);

        var address = addressService.findAllAddressByUserId(userId);

        var addressGetResponseList = mapper.toAddressGetResponseList(address);

        return ResponseEntity.ok().body(addressGetResponseList);
    }


    @PostMapping("{userId}")
    public ResponseEntity<AddressPostResponse> save(@Valid @RequestBody AddressPostRequest request,
                                                    @PathVariable UUID userId) {
        log.debug("request to save address for user: '{}'", userId);

        var addressResponse = addressService.save(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        log.debug("request to delete address with id: '{}'", id);

        var addressToDelete = addressService.findAddressByIdOrElseThrow(id);

        addressService.delete(addressToDelete);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressPutResponse> update(@Valid @RequestBody AddressPutRequest request,
                                                     @PathVariable UUID id) {
        log.debug("request to update address with id: '{}'", id);

        var addressUpdated = addressService.update(request, id);

        var addressPutResponse = mapper.toAddressPutResponse(addressUpdated);

        return ResponseEntity.ok().body(addressPutResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<AddressPatchResponse> updateSelectedFields(@Valid @RequestBody AddressPatchRequest request,
                                                                     @PathVariable UUID id) {
        log.debug("request to updateSelectedFields address with id: '{}'", id);

        var addressUpdate = addressService.updateSelectedFields(request, id);

        var addressPatchResponse = mapper.toAddressPatchResponse(addressUpdate);

        return ResponseEntity.ok().body(addressPatchResponse);
    }


}
