package it.unisalento.pasproject.notificationservice.service;

import it.unisalento.pasproject.notificationservice.domain.Notification;
import it.unisalento.pasproject.notificationservice.domain.PopupNotification;
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
            throw new IllegalArgumentException("Invalid notification type");
        }

        SseEmitter emitter = sseEmitters.get(popupNotification.getUserEmail());
        if (emitter != null) {
            try {
                emitter.send(popupNotification.getMessage());
            } catch (IOException e) {
                throw new RuntimeException("Error sending SSE", e);
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
