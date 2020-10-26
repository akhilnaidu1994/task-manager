package com.akn.taskmanager.controller;

import com.akn.taskmanager.model.User;
import com.akn.taskmanager.service.JWTTokenService;
import com.akn.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JWTTokenService jwtTokenService;

    @PostMapping("/register")
    public User register(@RequestBody @Valid User user){
        return userService.register(user);
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token){
        String username =  jwtTokenService.validateRefreshToken(token);
        if(username != null) {
          String accessToken = jwtTokenService.generateAccessToken(username);
          return new ResponseEntity<String>(accessToken, HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }
}
