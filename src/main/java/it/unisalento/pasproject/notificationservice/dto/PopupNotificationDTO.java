package it.unisalento.pasproject.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopupNotificationDTO extends NotificationDTO {
    private String userEmail;
}
