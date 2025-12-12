package dev.cong.v.springcomereme.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForgotPasswordRequest {

    private  String email;

    private  String password;

    private  String token;

    private  String otp;




}
