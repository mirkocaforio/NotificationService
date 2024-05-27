package it.unisalento.pasproject.notificationservice.controller;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import it.unisalento.pasproject.notificationservice.dto.NotificationListDTO;
import it.unisalento.pasproject.notificationservice.repositories.EmailNotificationRepository;
import it.unisalento.pasproject.notificationservice.repositories.PopupNotificationRepository;
import it.unisalento.pasproject.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final EmailNotificationRepository emailNotificationRepository;
    private final PopupNotificationRepository popupNotificationRepository;
    private final NotificationService notificationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    public NotificationController(NotificationService notificationService, EmailNotificationRepository emailNotificationRepository, PopupNotificationRepository popupNotificationRepository) {
        this.notificationService = notificationService;
        this.emailNotificationRepository = emailNotificationRepository;
        this.popupNotificationRepository = popupNotificationRepository;
    }

    //TODO: VEDERE COME GESTIRE I REPOSITORY PER FARE LE QUERY, SE SERVIRANNO

    @GetMapping(value = "/findEmail/all")
    public NotificationListDTO getAllEmailNotifications() {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        List<NotificationDTO> notificationList = new ArrayList<>();
        notificationListDTO.setNotificationsList(notificationList);

        List<EmailNotification> notifications = emailNotificationRepository.findAll();

        for (EmailNotification notification : notifications) {
            NotificationDTO notificationDTO = notificationService.getNotificationDTO(notification);
            notificationList.add(notificationDTO);
        }

        return notificationListDTO;
    }

    @GetMapping(value = "/findPopup/all")
    public NotificationListDTO getAllPopupNotifications() {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        List<NotificationDTO> notificationList = new ArrayList<>();
        notificationListDTO.setNotificationsList(notificationList);

        List<PopupNotification> notifications = popupNotificationRepository.findAll();

        for (PopupNotification notification : notifications) {
            NotificationDTO notificationDTO = notificationService.getNotificationDTO(notification);
            notificationList.add(notificationDTO);
        }

        return notificationListDTO;
    }
}
