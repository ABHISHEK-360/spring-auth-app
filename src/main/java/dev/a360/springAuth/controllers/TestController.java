package dev.a360.springAuth.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${welcome.message}")
    private String welcomeMessage;

    @RequestMapping("/")
    public String index(){
        return welcomeMessage;
    }
}
