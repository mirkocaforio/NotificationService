package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import it.unisalento.pasproject.notificationservice.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageHandler {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageHandler.class);

    @Autowired
    public NotificationMessageHandler(NotificationService notificationService, NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void receiveNotificationMessage(NotificationDTO notificationDTO) {
        //TODO: Serve ricevere il messaggio del DTO convertirlo in domain e salvarlo
        // successivamente usare il bridge per inviare la mail
        LOGGER.info("Received notification message");
        Notification notification = notificationService.getNotification(notificationDTO);
        notificationRepository.save(notification);

        //TODO: Invio mail e vedere se dare un tipo di ritorno
    }
}
