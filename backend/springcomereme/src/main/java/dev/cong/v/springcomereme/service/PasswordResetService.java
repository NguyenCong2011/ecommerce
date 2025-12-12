package dev.cong.v.springcomereme.service;


import dev.cong.v.springcomereme.dao.PasswordRepository;
import dev.cong.v.springcomereme.dao.UserRepository;
import dev.cong.v.springcomereme.entity.PasswordResetToken;
import dev.cong.v.springcomereme.entity.User;
import dev.cong.v.springcomereme.exception.EmailNotFoundException;
import dev.cong.v.springcomereme.exception.IncorrectRequestOTP;
import dev.cong.v.springcomereme.request.EmailRequest;
import dev.cong.v.springcomereme.request.ForgotPasswordRequest;
import dev.cong.v.springcomereme.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetService {

    private  final PasswordRepository passwordRepository;
    private  final UserRepository userRepository;
    private  final  EmailService emailService;
    private  final PasswordEncoder passwordEncoder;


    @Transactional
    public ResponseEntity<?> forgotProcessing(ForgotPasswordRequest request) throws NoSuchAlgorithmException {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Your email does not exist in our database"));



        // delete Before
        passwordRepository.deleteByUserId(user.getId());


        String token = generateToken();
        String otp = generateOTP();
        PasswordResetToken passwordResetToken = buildPasswordResetToken(user, token, otp);

        passwordRepository.save(passwordResetToken);

        if (sendOtpEmail(request.getEmail(), otp)) {
            AuthenticationResponse response = AuthenticationResponse.builder().token(token).build();
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body("Something went wrong");
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private PasswordResetToken buildPasswordResetToken(User user, String token, String otp) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 3);


        return PasswordResetToken.builder()
                .expiryDate(calendar.getTime())
                .token(token)
                .otp(otp)
                .user(user)
                .build();
    }

    private boolean sendOtpEmail(String email, String otp) {
        EmailRequest emailRequest = EmailRequest.builder()
                .toMail(email)
                .body("Your OTP to reset password is " + otp)
                .subject("OTP")
                .build();
        return emailService.sendMail(emailRequest);
    }

    private String generateOTP() {
        StringBuilder generatedOTP = new StringBuilder();
        Random random = new Random(); // Use java.util.Random as a fallback

        for (int i = 0; i < 6; i++) {
            generatedOTP.append(random.nextInt(10));
        }

        return generatedOTP.toString();
    }

    public ResponseEntity<?> validateOTP(ForgotPasswordRequest request) {

        String inputOTP = request.getOtp();

        String inputToken = request.getToken();

        var existing = this.passwordRepository.findByTokenAndOtp(inputToken,inputOTP).orElseThrow(
                ()-> new IncorrectRequestOTP("Your OTP is Not Correct")
        );

        // check for expiration

        if (existing.getExpiryDate().before(new Date())) {
            throw new IncorrectRequestOTP("Your OTP has expired");
        }
        String passwordResetToken = UUID.randomUUID().toString();
        existing.setToken(passwordResetToken);
        passwordRepository.save(existing);
        return  ResponseEntity.ok(AuthenticationResponse.builder().token(passwordResetToken).build());

    }


    public ResponseEntity<?> reset(ForgotPasswordRequest request) {

        var existing = this.passwordRepository.findByToken(request.getToken()).orElseThrow(
                ()-> new IncorrectRequestOTP("FORBIDDEN")
        );

        var user = existing.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        passwordRepository.delete(existing);


        return  ResponseEntity.ok("Success");


    }
}
