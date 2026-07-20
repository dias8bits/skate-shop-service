package com.skateshop.mapper.user;

import com.skateshop.domain.user.Address;
import com.skateshop.dto.request.AddressPatchRequest;
import com.skateshop.dto.request.AddressPostRequest;
import com.skateshop.dto.request.AddressPutRequest;
import com.skateshop.dto.response.AddressGetResponse;
import com.skateshop.dto.response.AddressPatchResponse;
import com.skateshop.dto.response.AddressPostResponse;
import com.skateshop.dto.response.AddressPutResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    Address toAddress(AddressPostRequest request);

    @Mapping(target = "userId", source = "user.id")
    AddressPostResponse toAddressPostResponse(Address address);

    Address toAddress(AddressPutRequest request);

    @Mapping(target = "userId", source = "user.id")
    AddressPutResponse toAddressPutResponse(Address address);

    Address toAddress(AddressPatchRequest request);

    @Mapping(target = "userId", source = "user.id")
    AddressPatchResponse toAddressPatchResponse(Address address);

    @Mapping(target = "userId", source = "user.id")
    List<AddressGetResponse> toAddressGetResponseList(List<Address> address);

    @Mapping(target = "userId", source = "user.id")
    AddressGetResponse toAddressGetResponse(Address address);
}
