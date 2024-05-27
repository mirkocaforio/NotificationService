package it.unisalento.pasproject.notificationservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopupNotification extends Notification {
    private String userEmail;
}
