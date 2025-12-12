package dev.cong.v.springcomereme.controller;


import dev.cong.v.springcomereme.jwt.JwtService;
import dev.cong.v.springcomereme.request.AuthenticationRequest;
import dev.cong.v.springcomereme.request.ChangePasswordRequest;
import dev.cong.v.springcomereme.request.RegisterRequest;
import dev.cong.v.springcomereme.request.UpdateLogoRequest;
import dev.cong.v.springcomereme.response.AuthenticationResponse;
import dev.cong.v.springcomereme.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private  final AuthenticationService service;
    private  final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request, HttpServletResponse response){
        return  ResponseEntity.ok(service.authenticate(request,response));
    }

    @PutMapping("/update-logo")
    public  ResponseEntity<?> updateLogo(@RequestBody UpdateLogoRequest updateLogoRequest, HttpServletRequest request){

        String token = request.getHeader("Authorization").substring(7);

        String email = jwtService.extractUsername(token);
        return  service.updateLogo(updateLogoRequest.getPhotoUrl(),email);
    }

    @PostMapping("/change-password")
    public  ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest ,
                                             @AuthenticationPrincipal UserDetails userDetails){


        return  (service.changePassword(userDetails.getUsername(),changePasswordRequest));

    }

    @GetMapping("/refresh")
    private  ResponseEntity<?> refreshToken( HttpServletRequest request){

        return service.refreshToken(request);
    }
    @GetMapping("/logout")
    private  ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response){
        return service.logout(request,response);
    }


    @GetMapping("/admin/all-user")
    public  ResponseEntity<?> getAllUser(){
            return  service.getAllUser();
    }

    @DeleteMapping("/admin/user/{id}")
    public  ResponseEntity<?> deleteUser(@PathVariable("id")Long id){
        return  service.deleteUser(id);
    }
}
