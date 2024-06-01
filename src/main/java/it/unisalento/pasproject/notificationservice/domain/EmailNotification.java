package it.unisalento.pasproject.notificationservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "emailNotification")
public class EmailNotification extends Notification {
    private String attachment;
}
