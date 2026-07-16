package com.skateshop.controller;

import com.skateshop.dto.request.UserPostRequest;
import com.skateshop.dto.response.UserGetResponse;
import com.skateshop.dto.response.UserPostResponse;
import com.skateshop.mapper.UserMapper;
import com.skateshop.service.userService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    private final UserMapper mapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserGetResponse>> findById() {
        log.debug("request to list all users");

        var users = userService.findAll();

        var userGetResponseList = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponseList);
    }

    @GetMapping("{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserGetResponse> findUserByCpf(@PathVariable String cpf) {
        log.debug("request to find user with cpf: '{}'", cpf);

        var user = userService.findUserByCpfOrElseThrow(cpf);

        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserPostResponse> save(@Valid @RequestBody UserPostRequest request) {
        log.debug("request to save user");

        var userToSave = mapper.toUser(request);

        var userSaved = userService.save(userToSave);

        var userPostResponse = mapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        log.debug("request to delete user");

        var userToDelete = userService.findUserByIdOrElseThrow(id);

        userService.delete(userToDelete);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> update() {
        return null;
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateSelectedFields() {
        return null;
    }

}
