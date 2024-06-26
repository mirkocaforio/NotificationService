package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.domain.NotificationFactory;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.dto.*;
import it.unisalento.pasproject.notificationservice.repositories.EmailNotificationRepository;
import it.unisalento.pasproject.notificationservice.repositories.PopupNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationFactory notificationFactory;
    private final NotificationDTOFactory notificationDTOFactory;
    private final MongoTemplate mongoTemplate;
    private final EmailNotificationRepository emailNotificationRepository;
    private final PopupNotificationRepository popupNotificationRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(MongoTemplate mongoTemplate, EmailNotificationRepository emailNotificationRepository, PopupNotificationRepository popupNotificationRepository) {
        this.notificationFactory = new NotificationFactory();
        this.notificationDTOFactory = new NotificationDTOFactory();
        this.mongoTemplate = mongoTemplate;
        this.emailNotificationRepository = emailNotificationRepository;
        this.popupNotificationRepository = popupNotificationRepository;

    }

    public EmailNotification getEmailNotification(NotificationMessageDTO notificationMessageDTO) {
        EmailNotification emailNotification = (EmailNotification) notificationFactory.getNotificationType(NotificationFactory.NotificationType.EMAIL);

        Optional.ofNullable(notificationMessageDTO.getReceiver()).ifPresent(emailNotification::setEmail);
        Optional.ofNullable(notificationMessageDTO.getSubject()).ifPresent(emailNotification::setSubject);
        Optional.ofNullable(notificationMessageDTO.getMessage()).ifPresent(emailNotification::setMessage);
        //TODO: VEDERE COME GESTIRE EVENTUALI ALLEGATI
        //Optional.ofNullable(notificationMessageDTO.getAttachment()).ifPresent(emailNotification::setAttachment);
        Optional.ofNullable(notificationMessageDTO.getType()).ifPresent(emailNotification::setType);
        emailNotification.setSendAt(LocalDateTime.now());

        return emailNotificationRepository.save(emailNotification);
    }

    public PopupNotification getPopupNotification(NotificationMessageDTO notificationMessageDTO) {
        PopupNotification popupNotification = (PopupNotification) notificationFactory.getNotificationType(NotificationFactory.NotificationType.POPUP);

        Optional.ofNullable(notificationMessageDTO.getReceiver()).ifPresent(popupNotification::setEmail);
        Optional.ofNullable(notificationMessageDTO.getSubject()).ifPresent(popupNotification::setSubject);
        Optional.ofNullable(notificationMessageDTO.getMessage()).ifPresent(popupNotification::setMessage);
        Optional.ofNullable(notificationMessageDTO.getType()).ifPresent(popupNotification::setType);
        popupNotification.setSendAt(LocalDateTime.now());
        popupNotification.setRead(false);

        return popupNotificationRepository.save(popupNotification);
    }

    public NotificationDTO getNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO;
        if (notification instanceof EmailNotification emailNotification) {

            EmailNotificationDTO emailNotificationDTO = (EmailNotificationDTO) notificationDTOFactory.getNotificationDTOType(NotificationDTOFactory.NotificationDTOType.EMAIL);

            Optional.ofNullable(emailNotification.getId()).ifPresent(emailNotificationDTO::setId);
            Optional.ofNullable(emailNotification.getEmail()).ifPresent(emailNotificationDTO::setEmail);
            Optional.ofNullable(emailNotification.getSubject()).ifPresent(emailNotificationDTO::setSubject);
            Optional.ofNullable(emailNotification.getMessage()).ifPresent(emailNotificationDTO::setMessage);
            Optional.ofNullable(emailNotification.getAttachment()).ifPresent(emailNotificationDTO::setAttachment);
            Optional.ofNullable(emailNotification.getSendAt()).ifPresent(emailNotificationDTO::setSendAt);

            notificationDTO = emailNotificationDTO;
        } else if (notification instanceof PopupNotification popupNotification) {
            PopupNotificationDTO popupNotificationDTO = (PopupNotificationDTO) notificationDTOFactory.getNotificationDTOType(NotificationDTOFactory.NotificationDTOType.POPUP);

            Optional.ofNullable(popupNotification.getId()).ifPresent(popupNotificationDTO::setId);
            Optional.ofNullable(popupNotification.getEmail()).ifPresent(popupNotificationDTO::setEmail);
            Optional.ofNullable(popupNotification.getSubject()).ifPresent(popupNotificationDTO::setSubject);
            Optional.ofNullable(popupNotification.getMessage()).ifPresent(popupNotificationDTO::setMessage);
            Optional.ofNullable(popupNotification.getSendAt()).ifPresent(popupNotificationDTO::setSendAt);
            Optional.of(popupNotification.isRead()).ifPresent(popupNotificationDTO::setRead);

            notificationDTO = popupNotificationDTO;
        } else {
            throw new IllegalArgumentException("Unsupported notification type");
        }

        return notificationDTO;
    }

    public NotificationDTO updateReadStatus(String id, boolean read) {
        Optional<PopupNotification> optionalPopupNotification = popupNotificationRepository.findById(id);
        if (optionalPopupNotification.isPresent()) {
            PopupNotification popupNotification = optionalPopupNotification.get();
            popupNotification.setRead(read);
            popupNotificationRepository.save(popupNotification);

            return getNotificationDTO(popupNotification);
        } else {
            return null;
        }
    }

    public void updateAllReadStatus(boolean read) {
        Optional<List<PopupNotification>> popupNotifications = popupNotificationRepository.findByRead(!read);

        if(popupNotifications.isEmpty()) {
            return;
        }

        List<PopupNotification> popups = popupNotifications.get();

        for (PopupNotification popupNotification : popups) {
            popupNotification.setRead(read);
        }
        popupNotificationRepository.saveAll(popups);
    }

    public List<EmailNotification> findEmailNotifications(String email, String subject, LocalDateTime from, LocalDateTime to) {
        Query query = new Query();

        // Add conditions based on parameters provided
        if (email != null) {
            query.addCriteria(Criteria.where("email").is(email));
        }

        if (subject != null) {
            query.addCriteria(Criteria.where("subject").is(subject));
        }
        if (from != null) {
            query.addCriteria(Criteria.where("sendAt").gte(from));
        }
        if (to != null) {
            query.addCriteria(Criteria.where("sendAt").lte(to));
        }

        LOGGER.info("\n{}\n", query);

        List<EmailNotification> emailNotifications = mongoTemplate.find(query, EmailNotification.class, mongoTemplate.getCollectionName(EmailNotification.class));

        LOGGER.info("\nEmail Notifications: {}\n", emailNotifications);

        return emailNotifications;
    }

    public List<PopupNotification> findPopupNotifications(String email, String subject, LocalDateTime from, LocalDateTime to, Boolean read) {
        Query query = new Query();

        // Add conditions based on parameters provided
        if (email != null) {
            query.addCriteria(Criteria.where("email").is(email));
        }

        if (subject != null) {
            query.addCriteria(Criteria.where("subject").is(subject));
        }
        if (from != null) {
            query.addCriteria(Criteria.where("sendAt").gte(from));
        }
        if (to != null) {
            query.addCriteria(Criteria.where("sendAt").lte(to));
        }

        if (read != null) {
            query.addCriteria(Criteria.where("read").is(read));
        }

        LOGGER.info("\n{}\n", query);

        List<PopupNotification> popupNotifications = mongoTemplate.find(query, PopupNotification.class, mongoTemplate.getCollectionName(PopupNotification.class));

        LOGGER.info("\nPopup Notifications: {}\n", popupNotifications);

        return popupNotifications;
    }
}
