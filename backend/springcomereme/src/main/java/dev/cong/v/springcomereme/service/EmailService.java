package dev.cong.v.springcomereme.service;

import dev.cong.v.springcomereme.request.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class EmailService {

    private final   JavaMailSender sender;

    @Value("${spring.mail.username}")
    private   String formMail;

    public   boolean  sendMail(EmailRequest request){

       try {
           SimpleMailMessage mailMessage = new SimpleMailMessage();

           mailMessage.setSubject(request.getSubject());

           mailMessage.setFrom(formMail);

           mailMessage.setText(request.getBody());

           mailMessage.setTo(request.getToMail());

           sender.send(mailMessage);

           return  true;
       }catch (Exception e){
           System.out.println("ERROR " +e.getMessage());

           return  false;


       }

    }




}
