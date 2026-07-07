package com.eddiecwh.NotificationSystem.controller;

import com.eddiecwh.NotificationSystem.entity.User;
import com.eddiecwh.NotificationSystem.model.UserRequestBody;
import com.eddiecwh.NotificationSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequestBody request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId, @RequestBody UserRequestBody requestBody) {
        return new ResponseEntity<>(userService.updateUserById(userId, requestBody), HttpStatus.OK);
    }
}
