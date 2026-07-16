package com.skateshop.controller;

import com.skateshop.dto.request.AddressPostRequest;
import com.skateshop.dto.response.AddressGetResponse;
import com.skateshop.dto.response.AddressPostResponse;
import com.skateshop.mapper.AddressMapper;
import com.skateshop.service.BrasilApiService;
import com.skateshop.service.userService.AddressService;
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
    private final BrasilApiService brasilApiService;
    private final AddressMapper mapper;

    @GetMapping()
    public ResponseEntity<List<AddressGetResponse>> findAll(@RequestParam(required = false) UUID id) {
        log.debug("request to find all address ");

        var address = addressService.findAll(id);

        var addressGetResponseList = mapper.toAddressGetResponseList(address);

        return ResponseEntity.ok().body(addressGetResponseList);
    }


    @GetMapping("{userId}")
    public ResponseEntity<List<AddressGetResponse>> findAllAddressByUserId(@PathVariable UUID userId) {
        log.debug("request to find all address by user id: '{}'", userId);

        var address = addressService.findAllAddressByUserId(userId);

        var addressGetResponseList = mapper.toAddressGetResponseList(address);

        return ResponseEntity.ok().body(addressGetResponseList);
    }


    @PostMapping("user/{userId}")
    public ResponseEntity<AddressPostResponse> save(@Valid @RequestBody AddressPostRequest request,
                                                    @PathVariable UUID userId) {
        log.debug("request to save address for user: '{}'", userId);

        var addressResponse = addressService.save(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponse);
    }


}
