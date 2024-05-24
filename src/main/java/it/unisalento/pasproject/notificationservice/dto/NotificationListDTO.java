package it.unisalento.pasproject.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NotificationListDTO {
    private List<NotificationDTO> notificationsList;

    public NotificationListDTO() {
        this.notificationsList = new ArrayList<>();
    }
}
