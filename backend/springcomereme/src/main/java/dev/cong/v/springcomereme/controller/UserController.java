package dev.cong.v.springcomereme.controller;

import dev.cong.v.springcomereme.jwt.JwtService;
import dev.cong.v.springcomereme.dto.UserDTO;
import dev.cong.v.springcomereme.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private  final UserService service;
    private  final JwtService jwtService;


    @GetMapping("/auth")
    public ResponseEntity<?> getInformation(HttpServletRequest request){

        String token =request.getHeader("Authorization").substring(7);

        String email = jwtService.extractUsername(token);

        return  service.getInformation(email);


    }
    @PutMapping("/auth")
    public  ResponseEntity<?> updateInformation(@RequestBody UserDTO user, HttpServletRequest request){
        String token =request.getHeader("Authorization").substring(7);

        String email = jwtService.extractUsername(token);

        return  service.updateInformation(email,user);
    }



}
