package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class EmailNotificationSender implements NotificationSender {
    //TODO: Da capire come gestire l'eventuale invio di un allegato
    private final JavaMailSender mailSender;
    private final PdfCreator pdfCreator;

    @Autowired
    public EmailNotificationSender(JavaMailSender mailSender, PdfCreator pdfCreator) {
        this.mailSender = mailSender;
        this.pdfCreator = pdfCreator;
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
                ByteArrayOutputStream attachment = pdfCreator.createPdf(emailNotification.getAttachment());
                helper.addAttachment("attachment.pdf", new ByteArrayResource(attachment.toByteArray()));

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
