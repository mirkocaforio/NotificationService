package it.unisalento.pasproject.notificationservice.repositories;

import it.unisalento.pasproject.notificationservice.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
