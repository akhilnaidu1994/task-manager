package com.akn.taskmanager.controller;

import com.akn.taskmanager.model.User;
import com.akn.taskmanager.service.JWTTokenService;
import com.akn.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JWTTokenService jwtTokenService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        if (userService.getUser(user.getEmail()) != null) {
            return new ResponseEntity(null, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<User>(userService.register(user), HttpStatus.OK);
    }

    @PostMapping(value = "/token/refresh", produces = "text/plain")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token) {
        String username = jwtTokenService.validateRefreshToken(token);
        if (username != null) {
            String accessToken = jwtTokenService.generateAccessToken(username);
            return new ResponseEntity<String>(accessToken, HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping
    public ResponseEntity<User> getUser(@Autowired Principal principal) {
        User user = userService.getUser(principal.getName());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id){
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/search/{searchText}")
    public ResponseEntity<List<User>> searchUsers(@PathVariable("searchText") String searchText) {
        List<User> users = userService.searchUser(searchText);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
