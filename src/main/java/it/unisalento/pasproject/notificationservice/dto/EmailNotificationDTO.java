package it.unisalento.pasproject.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailNotificationDTO extends NotificationDTO {
    private String attachment;
}
