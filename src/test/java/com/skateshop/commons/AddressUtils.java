package com.skateshop.commons;

import com.skateshop.domain.user.Address;
import com.skateshop.domain.user.User;
import com.skateshop.dto.request.AddressPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddressUtils {

    private final UserUtils userUtils;

    public List<Address> getAddressList() {
        User user = userUtils.getUserList().getFirst();
        User user2 = userUtils.getUserList().getLast();

        var address1 = Address.builder()
                .id(UUID.fromString("09c351be-1482-4741-86e2-e9d8d65ef10f"))
                .number("23")
                .complement("2A")
                .cep("55555555")
                .state("sp")
                .city("sp")
                .street("av paulista")
                .neighborhood("centro")
                .user(user)
                .build();

        var address2 = Address.builder()
//                .id(UUID.fromString("610fc119-3f6d-47bd-9469-a5bc79c5e5c1"))
                .number("2")
                .complement("2B")
                .cep("77777777")
                .state("sp")
                .city("sp")
                .street("av paulista")
                .neighborhood("centro")
                .user(user2)
                .build();

        return List.of(address1, address2);
    }

    public AddressPostRequest newAddress() {
        return AddressPostRequest.builder()
//                .id(UUID.fromString("a674069b-fad0-4249-a83a-33dc548c2c52"))
                .number("3")
                .complement("3B")
                .cep("11111111")
//                .state("sp")
//                .city("sp")
//                .street("av paulista")
//                .neighborhood("centro")
//                .user(user)
                .build();

    }
}