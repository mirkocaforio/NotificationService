package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import it.unisalento.pasproject.notificationservice.repositories.EmailNotificationRepository;
import it.unisalento.pasproject.notificationservice.repositories.PopupNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageHandler {
    private final NotificationService notificationService;
    private final EmailNotificationRepository emailNotificationRepository;
    private final PopupNotificationRepository popupNotificationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageHandler.class);

    @Autowired
    public NotificationMessageHandler(NotificationService notificationService, EmailNotificationRepository emailNotificationRepository, PopupNotificationRepository popupNotificationRepository) {
        this.notificationService = notificationService;
        this.emailNotificationRepository = emailNotificationRepository;
        this.popupNotificationRepository = popupNotificationRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void receiveNotificationMessage(NotificationDTO notificationDTO) {
        LOGGER.info("Received notification message");
        Notification notification = notificationService.getNotification(notificationDTO);
        if (notification instanceof EmailNotification emailNotification) {
            emailNotificationRepository.save(emailNotification);
            LOGGER.info("Email notification message saved");
        } else if (notification instanceof PopupNotification popupNotification) {
            popupNotificationRepository.save(popupNotification);
            LOGGER.info("Popup notification message saved");
        } else {
            throw new IllegalArgumentException("Unsupported notification type");
        }

        //TODO: Invio mail e vedere se dare un tipo di ritorno
    }
}
