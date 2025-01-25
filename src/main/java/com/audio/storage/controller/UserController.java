package com.audio.storage.controller;


import com.audio.storage.common.MessageResponse;
import com.audio.storage.dto.UserRequestDto;
import com.audio.storage.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //create user
    @PostMapping
    public ResponseEntity<MessageResponse> create(@RequestBody UserRequestDto requestDto){
        return ResponseEntity.ok(new MessageResponse(userService.create(requestDto)));
    }



}
