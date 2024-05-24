package it.unisalento.pasproject.notificationservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "notification")
public class Notification {
    private String id;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime sendAt;
    //TODO: VEDERE COME GESTIRE EVENTUALI ALLEGATI
}
