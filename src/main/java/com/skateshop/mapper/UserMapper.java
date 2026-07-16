package com.skateshop.mapper;

import com.skateshop.domain.user.User;
import com.skateshop.dto.request.UserPostRequest;
import com.skateshop.dto.response.UserGetResponse;
import com.skateshop.dto.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserPostRequest request);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);

}
