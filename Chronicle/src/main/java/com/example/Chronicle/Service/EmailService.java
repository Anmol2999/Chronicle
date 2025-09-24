package com.example.Chronicle.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.Chronicle.util.email.emailDetails;

@Service
public class EmailService {
    
    @Autowired
    private  JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public boolean sendEmail(emailDetails details){
        try {
             SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(details.getRecipient());
        mailMessage.setSubject(details.getSubject());
        mailMessage.setText(details.getBody());

        mailSender.send(mailMessage);
        return true;
        } catch (Exception e) {
           return false;
        }
       

}
}