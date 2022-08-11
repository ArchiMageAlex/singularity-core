package com.nfcs.singularity.core.service;

import com.nfcs.singularity.core.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MailService {
    private static Logger LOG = Logger.getLogger(MailService.class.getName());
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;

    public MailService(@Autowired MailConfig mailConfig) {
        this.mailSender = mailConfig.getMailSender();
    }

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Mail does not sent", e);
        }
    }
}
