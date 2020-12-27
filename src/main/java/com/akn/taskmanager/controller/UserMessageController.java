package com.akn.taskmanager.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class UserMessageController {

    @MessageMapping("/hello")
    @SendToUser("")
    public String hello(String greeting){
        return "hello " + greeting;
    }
}
