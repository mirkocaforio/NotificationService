package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.domain.NotificationFactory;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import it.unisalento.pasproject.notificationservice.dto.NotificationMessageDTO;
import it.unisalento.pasproject.notificationservice.repositories.EmailNotificationRepository;
import it.unisalento.pasproject.notificationservice.repositories.PopupNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationMessageHandler {
    private final NotificationService notificationService;
    private final NotificationFactory notificationFactory;
    private final EmailNotificationRepository emailNotificationRepository;
    private final PopupNotificationRepository popupNotificationRepository;
    private final EmailNotificationSender emailNotificationSender;
    private final PopupNotificationSender popupNotificationSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageHandler.class);

    @Autowired
    public NotificationMessageHandler(NotificationService notificationService, EmailNotificationRepository emailNotificationRepository, PopupNotificationRepository popupNotificationRepository, EmailNotificationSender emailNotificationSender, PopupNotificationSender popupNotificationSender) {
        this.notificationService = notificationService;
        this.emailNotificationRepository = emailNotificationRepository;
        this.popupNotificationRepository = popupNotificationRepository;
        this.emailNotificationSender = emailNotificationSender;
        this.popupNotificationSender = popupNotificationSender;
        this.notificationFactory = new NotificationFactory();
    }

    @RabbitListener(queues = "${rabbitmq.queue.notification.name}")
    public void receiveNotificationMessage(NotificationMessageDTO notificationDTO) {
        LOGGER.info("Received notification message");

        if(notificationDTO.isNotification()) {
            PopupNotification popupNotification = (PopupNotification) notificationFactory.getNotificationType(NotificationFactory.NotificationType.POPUP);

            Optional.ofNullable(notificationDTO.getReceiver()).ifPresent(popupNotification::setEmail);
            Optional.ofNullable(notificationDTO.getSubject()).ifPresent(popupNotification::setSubject);
            Optional.ofNullable(notificationDTO.getMessage()).ifPresent(popupNotification::setMessage);
            popupNotification.setSendAt(LocalDateTime.now());

            popupNotificationRepository.save(popupNotification);
            LOGGER.info("Popup notification message saved");
        }

        if(notificationDTO.isEmail()) {
            EmailNotification emailNotification = (EmailNotification) notificationFactory.getNotificationType(NotificationFactory.NotificationType.EMAIL);

            Optional.ofNullable(notificationDTO.getReceiver()).ifPresent(emailNotification::setEmail);
            Optional.ofNullable(notificationDTO.getSubject()).ifPresent(emailNotification::setSubject);
            Optional.ofNullable(notificationDTO.getMessage()).ifPresent(emailNotification::setMessage);
            //TODO: VEDERE COME GESTIRE EVENTUALI ALLEGATI
            //Optional.ofNullable(notificationDTO.getAttachment()).ifPresent(emailNotification::setAttachment);
            emailNotification.setSendAt(LocalDateTime.now());

            emailNotificationRepository.save(emailNotification);
            emailNotificationSender.sendNotification(emailNotification);
            LOGGER.info("Email notification message saved");
        }

        if (!notificationDTO.isNotification() && !notificationDTO.isEmail()) {
            PopupNotification popupNotification = (PopupNotification) notificationFactory.getNotificationType(NotificationFactory.NotificationType.POPUP);

            Optional.ofNullable(notificationDTO.getReceiver()).ifPresent(popupNotification::setEmail);
            Optional.ofNullable(notificationDTO.getSubject()).ifPresent(popupNotification::setSubject);
            Optional.ofNullable(notificationDTO.getMessage()).ifPresent(popupNotification::setMessage);
            popupNotification.setSendAt(LocalDateTime.now());

            popupNotificationRepository.save(popupNotification);
            //popupNotificationSender.sendNotification(popupNotification);
        }

        //TODO: Invio mail e vedere se dare un tipo di ritorno
    }
}
