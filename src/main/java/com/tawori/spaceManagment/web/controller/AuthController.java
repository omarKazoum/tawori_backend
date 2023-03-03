package com.tawori.spaceManagment.web.controller;

import com.tawori.spaceManagment.dto.AuthDTO;
import com.tawori.spaceManagment.exceptions.RegisterException;
import com.tawori.spaceManagment.repository.UserRepository;
import com.tawori.spaceManagment.service.implementations.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;
    @PostMapping("/register")
    ResponseEntity register(@RequestBody AuthDTO userDTO){
        try{
            AuthDTO authDTO =userDetailsService.register(userDTO);
            return new ResponseEntity<>(authDTO, HttpStatus.ACCEPTED);

        }catch (RegisterException exception){
            exception.printStackTrace();
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    ResponseEntity login(@RequestBody AuthDTO loginDTO) {
        try {
            return new ResponseEntity<>(userDetailsService.login(loginDTO), HttpStatus.ACCEPTED);
        }catch (Exception exception){
            return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        }
    }


}
