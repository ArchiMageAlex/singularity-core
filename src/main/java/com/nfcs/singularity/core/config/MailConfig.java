package com.nfcs.singularity.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.logging.Logger;

@Configuration
public class MailConfig {
    private static Logger logger = Logger.getLogger(MailConfig.class.getName());

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttls;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(username);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.debug", debug);
        props.setProperty("mail.smtp.auth", auth);
        logger.info("Auth: " + auth);
        props.setProperty("mail.smtp.starttls.enable", starttls);


        return mailSender;
    }
}
