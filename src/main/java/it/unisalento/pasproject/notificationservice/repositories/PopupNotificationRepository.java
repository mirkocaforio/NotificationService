package it.unisalento.pasproject.notificationservice.repositories;

import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PopupNotificationRepository extends MongoRepository<PopupNotification, String> {
    Optional<List<PopupNotification>> findByRead(boolean read);
}
