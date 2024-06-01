package it.unisalento.pasproject.notificationservice.controller;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.NotificationDTO;
import it.unisalento.pasproject.notificationservice.dto.NotificationListDTO;
import it.unisalento.pasproject.notificationservice.exceptions.NotificationNotFoundException;
import it.unisalento.pasproject.notificationservice.repositories.EmailNotificationRepository;
import it.unisalento.pasproject.notificationservice.repositories.PopupNotificationRepository;
import it.unisalento.pasproject.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping(value = "/find/email/all")
    public NotificationListDTO getAllEmailNotifications() {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        List<NotificationDTO> notificationList = new ArrayList<>();
        notificationListDTO.setNotificationsList(notificationList);

        List<EmailNotification> notifications = emailNotificationRepository.findAll();

        for (EmailNotification notification : notifications) {
            notificationList.add(notificationService.getNotificationDTO(notification));
        }

        return notificationListDTO;
    }

    @GetMapping(value = "/find/popup/all")
    public NotificationListDTO getAllPopupNotifications() {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        List<NotificationDTO> notificationList = new ArrayList<>();
        notificationListDTO.setNotificationsList(notificationList);

        List<PopupNotification> notifications = popupNotificationRepository.findAll();

        for (PopupNotification notification : notifications) {
            notificationList.add(notificationService.getNotificationDTO(notification));
        }

        return notificationListDTO;
    }


    @GetMapping("/find/email")
    public NotificationListDTO getEmailByFilter(@RequestParam(required = false) String email,
                                           @RequestParam(required = false) String subject,
                                           @RequestParam(required = false) LocalDateTime from,
                                           @RequestParam(required = false) LocalDateTime to) {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        List<NotificationDTO> list = new ArrayList<>();
        notificationListDTO.setNotificationsList(list);

        List<EmailNotification> notifications = notificationService.findEmailNotifications(email, subject, from, to);

        if (notifications.isEmpty())
            throw new NotificationNotFoundException("No notifications found");

        for (Notification notification : notifications) {
            list.add(notificationService.getNotificationDTO(notification));
        }

        return notificationListDTO;
    }

    @GetMapping("/find/popup")
    public NotificationListDTO getPopupByFilter(@RequestParam(required = false) String email,
                                                @RequestParam(required = false) String subject,
                                                @RequestParam(required = false) LocalDateTime from,
                                                @RequestParam(required = false) LocalDateTime to,
                                                @RequestParam(required = false) Boolean read) {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        List<NotificationDTO> list = new ArrayList<>();
        notificationListDTO.setNotificationsList(list);

        List<PopupNotification> notifications = notificationService.findPopupNotifications(email, subject, from, to, read);

        if (notifications.isEmpty())
            throw new NotificationNotFoundException("No notifications found");

        for (Notification notification : notifications) {
            list.add(notificationService.getNotificationDTO(notification));
        }

        return notificationListDTO;
    }

    @PutMapping("/update/read/{id}/{read}")
    public NotificationDTO updateReadStatus(@PathVariable String id, @PathVariable boolean read) {
        NotificationDTO notificationDTO = notificationService.updateReadStatus(id, read);

        if (notificationDTO == null) {
            throw new NotificationNotFoundException("No notification found with id: " + id);
        }

        return notificationDTO;
    }

    @PutMapping("/update/read/all/{read}")
    public void updateAllReadStatus(@PathVariable boolean read) {
        notificationService.updateAllReadStatus(read);
    }
}
