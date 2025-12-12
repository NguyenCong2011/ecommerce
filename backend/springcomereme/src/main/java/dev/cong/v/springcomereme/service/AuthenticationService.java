package dev.cong.v.springcomereme.service;


import dev.cong.v.springcomereme.jwt.JwtService;
import dev.cong.v.springcomereme.dao.ShoppingCartRepository;
import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.dto.UserDTO;
import dev.cong.v.springcomereme.entity.ShoppingCart;
import dev.cong.v.springcomereme.entity.User;
import dev.cong.v.springcomereme.exception.ChangePasswordException;
import dev.cong.v.springcomereme.exception.EmailExitsException;
import dev.cong.v.springcomereme.request.AuthenticationRequest;
import dev.cong.v.springcomereme.request.ChangePasswordRequest;
import dev.cong.v.springcomereme.request.RegisterRequest;
import dev.cong.v.springcomereme.response.AuthenticationResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthenticationService {


    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private  final  AuthenticationManager  authenticationManager;
    private  final ShoppingCartRepository shoppingCartRepository;




    public AuthenticationResponse register(RegisterRequest request) {



        repository.findByEmail(request.getEmail())
                .ifPresent(user -> { throw new EmailExitsException("An account already exists for this email address"); });

        String name = request.getFirstName() + " "+ request.getLastName();
        ShoppingCart shoppingCart = new ShoppingCart();


        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .shoppingCart(shoppingCart)
                .role("ROLE_USER")
                .build();

        shoppingCart.setUser(user);

        var newUSer=  repository.save(user);


        var jwt = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .role(request.getRole())
                .id( newUSer.getId() )
                .name(name)
                .token(jwt)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {


        authenticateUser(request.getEmail(), request.getPassword());

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found for email: " + request.getEmail()));

        String role = user.getRole();

        String photoUrl = user.getPhotoURL();

        String name = user.getFirstName() +" " +user.getLastName();




        var jwtToken = jwtService.generateToken(user);

        //        Handle refresh token

         var refreshToken = jwtService.generateRefreshToken(user);


        // send cookie

        final ResponseCookie responseCookie =  ResponseCookie
                .from("jwt",refreshToken)
                .secure(true)
                .httpOnly(true)
                .path("/auth")
                .maxAge(60*60).sameSite("None")
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());




        user.setRefreshToken(refreshToken);

        repository.save(user);


        return buildAuthenticationResponse(user.getId(),role, jwtToken,photoUrl,name);
    }

    private void authenticateUser(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password", e);
        }
    }

    private AuthenticationResponse  buildAuthenticationResponse(Long id,String role, String token,String photoUrl,String name) {
        return AuthenticationResponse.builder()
                .id(id)
                .role(role)
                .photoUrl(photoUrl)
                .name(name)
                .token(token)
                .build();
    }

    public ResponseEntity<?> changePassword(String email, ChangePasswordRequest changePasswordRequest) {

        var user  = repository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Not Found User"));

        String oldPassword= user.getPassword();

        String newPassword = changePasswordRequest.getNewPassword();

        String currentPassword = changePasswordRequest.getCurrentPassword();


        if(!passwordEncoder.matches(currentPassword,oldPassword)){
            throw  new ChangePasswordException("Password does not match");
        }

        if (passwordEncoder.matches(newPassword, oldPassword)) {
           throw  new ChangePasswordException("New password must be different from the old password");

        }

        user.setPassword(passwordEncoder.encode(newPassword));

        repository.save(user);

        return   ResponseEntity.ok().body("Changed password successfully");

    }

    public ResponseEntity<?> updateLogo(String photoUrl, String email) {


        var user  = repository.findByEmail(email).orElseThrow(()-> new RuntimeException("Not Found User"));

        user.setPhotoURL(photoUrl);

        repository.save(user);

        return  ResponseEntity.ok("Updated");

    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("No Cookie");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No cookies found");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            System.out.println("No Refresh");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token found in cookies");
        }

        var user = repository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            System.out.println("Invalid");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthenticationResponse.builder().token(newAccessToken).build());
    }


    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No cookies found");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No refresh token found in cookies");
        }

        var user = repository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        user.setRefreshToken(null);
        repository.save(user);

        final ResponseCookie responseCookie = ResponseCookie.from("jwt", "")
                .secure(true)
                .httpOnly(true)
                .path("/auth")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Logged out successfully");
    }

    public ResponseEntity<?> getAllUser() {

        List<User> users = repository.findAll().stream().filter(user -> user.getRole().equals("ROLE_USER")).toList();

        List<UserDTO> userDTOS = users.stream().map(UserDTO::toDTO)
                .toList();

        return  ResponseEntity.ok(userDTOS);

    }

    public ResponseEntity<?> deleteUser(Long id) {
        var user = repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not found user")
        );

        repository.delete(user);

        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");


    }
}
