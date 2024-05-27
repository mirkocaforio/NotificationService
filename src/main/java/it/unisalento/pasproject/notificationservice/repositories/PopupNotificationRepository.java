package it.unisalento.pasproject.notificationservice.repositories;

import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PopupNotificationRepository extends MongoRepository<PopupNotification, String>{
}
