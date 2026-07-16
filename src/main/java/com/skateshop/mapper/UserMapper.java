package com.skateshop.mapper;

import com.skateshop.domain.user.User;
import com.skateshop.dto.request.UserPatchRequest;
import com.skateshop.dto.request.UserPostRequest;
import com.skateshop.dto.request.UserPutRequest;
import com.skateshop.dto.response.UserGetResponse;
import com.skateshop.dto.response.UserPatchResponse;
import com.skateshop.dto.response.UserPostResponse;
import com.skateshop.dto.response.UserPutResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserPostRequest request);

    UserPostResponse toUserPostResponse(User user);

    User toUser(UserPutRequest request);

    UserPutResponse toUserPutResponse(User user);

    User toUser(UserPatchRequest request);

    UserPatchResponse toUserPatchResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);

}
