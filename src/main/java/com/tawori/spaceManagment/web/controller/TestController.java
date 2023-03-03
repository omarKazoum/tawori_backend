package com.tawori.spaceManagment.web.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    ResponseEntity<String> test(){
        return ResponseEntity.ok("oki");
    }
}
