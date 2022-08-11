package com.nfcs.singularity.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.logging.Logger;

@Component
@PropertySource(value={"classpath:application.properties"})
public class MailConfig {
    private static Logger logger = Logger.getLogger(MailConfig.class.getName());
    private String username;
    private String host;
    private String password;
    private String port;
    private String protocol;
    private String debug;
    private String auth;
    private String starttls;

    @Autowired
    public MailConfig(@Value("${spring.mail.username}") String username,
                      @Value("${spring.mail.host}") String host,
                      @Value("${spring.mail.password}") String password,
                      @Value("${spring.mail.port}") String port,
                      @Value("${spring.mail.protocol}") String protocol,
                      @Value("${mail.debug}") String debug,
                      @Value("${spring.mail.properties.mail.smtp.auth}") String auth,
                      @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String starttls) {
        this.username = username;
        this.host = host;
        this.password = password;
        this.port = port;
        this.protocol = protocol;
        this.debug = debug;
        this.auth = auth;
        this.starttls = starttls;
    }

    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(username);
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.debug", debug);
        props.setProperty("mail.smtp.auth", auth);
        props.setProperty("mail.smtp.starttls.enable", starttls);

        return mailSender;
    }
}
