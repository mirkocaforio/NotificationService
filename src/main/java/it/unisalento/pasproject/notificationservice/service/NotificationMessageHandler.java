package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.NotificationMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageHandler {
    private final NotificationService notificationService;
    private final EmailNotificationSender emailNotificationSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageHandler.class);

    @Autowired
    public NotificationMessageHandler(NotificationService notificationService, EmailNotificationSender emailNotificationSender) {
        this.notificationService = notificationService;
        this.emailNotificationSender = emailNotificationSender;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notification.name}")
    public void receiveNotificationMessage(NotificationMessageDTO notificationDTO) {
        LOGGER.info("Received notification message");

        if(notificationDTO.isNotification()) {
            PopupNotification popupNotification = notificationService.getPopupNotification(notificationDTO);
            //popupNotificationSender.sendNotification(popupNotification);
            LOGGER.info("Popup notification message saved");
        }

        if(notificationDTO.isEmail()) {
            EmailNotification emailNotification = notificationService.getEmailNotification(notificationDTO);
            emailNotificationSender.sendNotification(emailNotification);
            LOGGER.info("Email notification message saved");
        }

        if (!notificationDTO.isNotification() && !notificationDTO.isEmail()) {
            PopupNotification popupNotification = notificationService.getPopupNotification(notificationDTO);
            //popupNotificationSender.sendNotification(popupNotification);
        }

        //TODO: Invio mail e vedere se dare un tipo di ritorno
    }
}
