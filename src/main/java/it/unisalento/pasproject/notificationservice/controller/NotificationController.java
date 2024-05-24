package it.unisalento.pasproject.notificationservice.controller;

import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import it.unisalento.pasproject.notificationservice.dto.NotificationListDTO;
import it.unisalento.pasproject.notificationservice.repositories.NotificationRepository;
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
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    public NotificationController(NotificationRepository notificationRepository, NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/find/all")
    public NotificationListDTO getAllNotifications() {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        List<NotificationDTO> notificationList = new ArrayList<>();
        notificationListDTO.setNotificationsList(notificationList);

        List<Notification> notifications = notificationRepository.findAll();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = notificationService.getNotificationDTO(notification);
            notificationList.add(notificationDTO);
        }

        return notificationListDTO;
    }
}
