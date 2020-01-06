package net.atos.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class AutoMailingService {
    private JavaMailSender javaMailSender;

    @Autowired
    public AutoMailingService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(String to, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Verification code - password recovery");
        simpleMailMessage.setText(message);

        javaMailSender.send(simpleMailMessage);
    }

}
