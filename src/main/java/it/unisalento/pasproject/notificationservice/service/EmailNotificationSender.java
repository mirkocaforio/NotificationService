package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailNotificationSender implements NotificationSender {
    //TODO: Da capire come gestire l'eventuale invio di un allegato
    private final JavaMailSender mailSender;

    @Autowired
    public EmailNotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotification(Notification notification) {
        if (!(notification instanceof EmailNotification emailNotification)) {
            throw new IllegalArgumentException("Invalid notification type");
        }

        if (emailNotification.getAttachment() != null) {
            // Se l'email ha un allegato, utilizza MimeMessage per inviare l'email
            MimeMessage message = mailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(emailNotification.getEmail());
                helper.setSubject(emailNotification.getSubject());
                helper.setText(notification.getMessage());

                // Aggiungi un allegato al messaggio
                FileSystemResource file = new FileSystemResource(new File(emailNotification.getAttachment()));
                //TODO: Nome del file di attachment totalmente campato in aria per ora
                helper.addAttachment("Attachment", file);

                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Error sending email", e);
            }
        } else {
            // Se l'email non ha un allegato, utilizza SimpleMailMessage per inviare l'email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailNotification.getEmail());
            message.setSubject(emailNotification.getSubject());
            message.setText(notification.getMessage());

            mailSender.send(message);
        }
    }
}
