package it.unisalento.pasproject.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class NotificationDTO {
    private String id;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime sendAt;
    private String type;
}
