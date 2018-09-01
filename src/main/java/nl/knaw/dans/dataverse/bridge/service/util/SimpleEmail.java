package nl.knaw.dans.dataverse.bridge.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmail {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleEmail.class);
    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;


    public void sendToAdmin(final String subject, final String message) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(env.getProperty("bridge.apps.support.email.send.to"));
        email.setSubject(subject);
        email.setText(message );
        email.setFrom(env.getProperty("bridge.apps.support.email.from"));
        LOG.info("Trying to send email to: " + env.getProperty("bridge.apps.support.email.send.to"));
        mailSender.send(email);
        LOG.info("Email send to: " + env.getProperty("bridge.apps.support.email.send.to"));
        LOG.info("Email message: " + message);
    }
}
