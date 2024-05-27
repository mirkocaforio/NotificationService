package it.unisalento.pasproject.notificationservice.repositories;

import it.unisalento.pasproject.notificationservice.domain.EmailNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailNotificationRepository extends MongoRepository<EmailNotification, String> {
}
