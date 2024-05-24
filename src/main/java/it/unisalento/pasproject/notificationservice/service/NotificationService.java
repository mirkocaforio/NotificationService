package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public Notification getNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setEmail(notificationDTO.getEmail());
        notification.setSubject(notificationDTO.getSubject());
        notification.setMessage(notificationDTO.getMessage());
        notification.setSendAt(notificationDTO.getSendAt());
        return notification;
    }

    public NotificationDTO getNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEmail(notification.getEmail());
        notificationDTO.setSubject(notification.getSubject());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setSendAt(notification.getSendAt());
        return notificationDTO;
    }
}
