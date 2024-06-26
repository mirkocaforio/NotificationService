package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
import it.unisalento.pasproject.notificationservice.exceptions.InvalidNotificationTypeException;
import it.unisalento.pasproject.notificationservice.exceptions.SseSendException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PopupNotificationSender implements NotificationSender {
    //TODO: VEDERE SE VA BENE QUESTA IMPLEMENTAZIONE

    // Mappa che associa ogni ID utente al suo SseEmitter
    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    public void sendNotification(Notification notification) {
        if (!(notification instanceof PopupNotification popupNotification)) {
            throw new InvalidNotificationTypeException("Invalid notification type");
        }

        SseEmitter emitter = sseEmitters.get(popupNotification.getEmail());
        if (emitter != null) {
            try {
                emitter.send(popupNotification);
            } catch (IOException e) {
                throw new SseSendException("Error sending SSE " + e.getMessage());
            }
        }
    }

    // Metodo per registrare un nuovo SseEmitter per un utente
    public void register(String userId, SseEmitter emitter) {
        sseEmitters.put(userId, emitter);
    }

    // Metodo per rimuovere un SseEmitter quando la connessione viene chiusa
    public void deregister(String userId) {
        sseEmitters.remove(userId);
    }
}
