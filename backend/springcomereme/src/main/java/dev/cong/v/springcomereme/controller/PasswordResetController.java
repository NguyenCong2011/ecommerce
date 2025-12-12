package dev.cong.v.springcomereme.controller;


import dev.cong.v.springcomereme.request.ForgotPasswordRequest;
import dev.cong.v.springcomereme.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/password")
public class PasswordResetController {

    private  final PasswordResetService service;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest request) throws NoSuchAlgorithmException {
        return  service.forgotProcessing(request);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody ForgotPasswordRequest request)  {
        return  service.validateOTP(request);
    }
    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ForgotPasswordRequest request) {
        return  service.reset(request);
    }

}
