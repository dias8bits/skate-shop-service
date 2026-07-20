package com.skateshop.controller.user;

import com.skateshop.dto.request.UserPatchRequest;
import com.skateshop.dto.request.UserPostRequest;
import com.skateshop.dto.request.UserPutRequest;
import com.skateshop.dto.response.UserGetResponse;
import com.skateshop.dto.response.UserPatchResponse;
import com.skateshop.dto.response.UserPostResponse;
import com.skateshop.dto.response.UserPutResponse;
import com.skateshop.mapper.user.UserMapper;
import com.skateshop.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    private final UserMapper mapper;

    @GetMapping()
    public ResponseEntity<List<UserGetResponse>> findAllOrById(@RequestParam(required = false) UUID id) {
        log.debug("request to list all users, param id: '{}'", id);

        var users = userService.findAll(id);

        var userGetResponseList = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponseList);
    }

    @GetMapping("{cpf}")
    public ResponseEntity<UserGetResponse> findUserByCpf(@PathVariable String cpf) {
        log.debug("request to find user with cpf: '{}'", cpf);

        var user = userService.findUserByCpfOrElseThrow(cpf);

        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping()
    public ResponseEntity<UserPostResponse> save(@Valid @RequestBody UserPostRequest request) {
        log.debug("request to save user");

        var userToSave = mapper.toUser(request);

        var userSaved = userService.save(userToSave);

        var userPostResponse = mapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        log.debug("request to delete user with id: '{}'", id);

        var userToDelete = userService.findUserByIdOrElseThrow(id);

        userService.delete(userToDelete);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<UserPutResponse> update(@Valid @RequestBody UserPutRequest request,
                                                  @PathVariable UUID id) {
        log.debug("request to update user with id: '{}'", id);

        var userToUpdate = mapper.toUser(request);

        var userUpdated = userService.update(userToUpdate, id);

        var userPutResponse = mapper.toUserPutResponse(userUpdated);

        return ResponseEntity.ok().body(userPutResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserPatchResponse> updateSelectedFields(@Valid @RequestBody UserPatchRequest request,
                                                                  @PathVariable UUID id) {
        log.debug("request to updateSelectedFields user with id: '{}'", id);

        var userToUpdate = mapper.toUser(request);

        var userUpdated = userService.updateSelectedFields(userToUpdate, id);

        var userPutResponse = mapper.toUserPatchResponse(userUpdated);

        return ResponseEntity.ok().body(userPutResponse);
    }

}
