package be.technobel.ylorth.reservastock_rest.bll.service.impl;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

//@Component
public class EmailServiceImpl {

    private final MailSender mailSender;

    public EmailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(String to, String subject, String text){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@reservastock.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e);
            throw new IllegalStateException("failed to send email");
        }


    }


}
