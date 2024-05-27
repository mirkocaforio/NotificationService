package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.Notification;

public interface NotificationSender {
    void sendNotification(Notification notification);
}
