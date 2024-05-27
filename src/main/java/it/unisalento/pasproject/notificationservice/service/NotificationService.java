package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.domain.NotificationFactory;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.EmailNotificationDTO;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTOFactory;
import it.unisalento.pasproject.notificationservice.dto.PopupNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private NotificationFactory notificationFactory;
    private NotificationDTOFactory notificationDTOFactory;

    public Notification getNotification(NotificationDTO notificationDTO) {
        Notification notification;

        if (notificationDTO instanceof EmailNotificationDTO emailNotificationDTO) {
            EmailNotification emailNotification = (EmailNotification) notificationFactory.getNotificationType(NotificationFactory.NotificationType.EMAIL);

            Optional.ofNullable(emailNotificationDTO.getEmail()).ifPresent(emailNotification::setEmail);
            Optional.ofNullable(emailNotificationDTO.getSubject()).ifPresent(emailNotification::setSubject);
            Optional.ofNullable(emailNotificationDTO.getAttachment()).ifPresent(emailNotification::setAttachment);
            Optional.ofNullable(emailNotificationDTO.getMessage()).ifPresent(emailNotification::setMessage);
            Optional.ofNullable(emailNotificationDTO.getSendAt()).ifPresent(emailNotification::setSendAt);

            notification = emailNotification;
        } else if (notificationDTO instanceof PopupNotificationDTO popupNotificationDTO) {
            PopupNotification popupNotification = (PopupNotification) notificationFactory.getNotificationType(NotificationFactory.NotificationType.POPUP);

            Optional.ofNullable(popupNotificationDTO.getUserEmail()).ifPresent(popupNotification::setUserEmail);
            Optional.ofNullable(popupNotificationDTO.getMessage()).ifPresent(popupNotification::setMessage);
            Optional.ofNullable(popupNotificationDTO.getSendAt()).ifPresent(popupNotification::setSendAt);

            notification = popupNotification;
        } else {
            throw new IllegalArgumentException("Unsupported notification type");
        }

        return notification;
    }

    public NotificationDTO getNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO;
        if (notification instanceof EmailNotification emailNotification) {

            EmailNotificationDTO emailNotificationDTO = (EmailNotificationDTO) notificationDTOFactory.getNotificationDTOType(NotificationDTOFactory.NotificationDTOType.EMAIL);

            Optional.ofNullable(emailNotification.getEmail()).ifPresent(emailNotificationDTO::setEmail);
            Optional.ofNullable(emailNotification.getSubject()).ifPresent(emailNotificationDTO::setSubject);
            Optional.ofNullable(emailNotification.getAttachment()).ifPresent(emailNotificationDTO::setAttachment);
            Optional.ofNullable(notification.getMessage()).ifPresent(emailNotificationDTO::setMessage);
            Optional.ofNullable(notification.getSendAt()).ifPresent(emailNotificationDTO::setSendAt);

            notificationDTO = emailNotificationDTO;
        } else if (notification instanceof PopupNotification popupNotification) {
            PopupNotificationDTO popupNotificationDTO = (PopupNotificationDTO) notificationDTOFactory.getNotificationDTOType(NotificationDTOFactory.NotificationDTOType.POPUP);

            Optional.ofNullable(popupNotification.getUserEmail()).ifPresent(popupNotificationDTO::setUserEmail);
            Optional.ofNullable(notification.getMessage()).ifPresent(popupNotificationDTO::setMessage);
            Optional.ofNullable(notification.getSendAt()).ifPresent(popupNotificationDTO::setSendAt);

            notificationDTO = popupNotificationDTO;
        } else {
            throw new IllegalArgumentException("Unsupported notification type");
        }

        return notificationDTO;
    }
}
