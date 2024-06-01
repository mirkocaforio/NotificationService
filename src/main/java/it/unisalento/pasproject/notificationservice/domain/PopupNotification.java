package it.unisalento.pasproject.notificationservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "popupNotification")
public class PopupNotification extends Notification {
    private boolean read;
}
