package it.unisalento.pasproject.notificationservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Notification {
    @Id
    private String id;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime sendAt;
    //TODO: VEDERE COME GESTIRE EVENTUALI ALLEGATI
}
