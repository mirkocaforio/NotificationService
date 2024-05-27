package it.unisalento.pasproject.notificationservice.dto;

public class NotificationDTOFactory {
    public enum NotificationDTOType { EMAIL, POPUP }

    public NotificationDTO getNotificationDTOType(NotificationDTOType type) {
        if (type == null) {
            type = NotificationDTOType.POPUP;
        }

        return switch (type) {
            case EMAIL -> new EmailNotificationDTO();
            case POPUP -> new PopupNotificationDTO();
        };
    }
}
