package it.unisalento.pasproject.notificationservice.domain;

public class NotificationFactory {
    public enum NotificationType { EMAIL, POPUP }

    public Notification getNotificationType(NotificationType type) {
        if (type == null) {
            type = NotificationType.POPUP;
        }

        return switch (type) {
            case EMAIL -> new EmailNotification();
            case POPUP -> new PopupNotification();
        };
    }
}
