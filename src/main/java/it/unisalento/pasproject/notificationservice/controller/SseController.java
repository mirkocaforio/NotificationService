package it.unisalento.pasproject.notificationservice.controller;

import it.unisalento.pasproject.notificationservice.service.PopupNotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
public class SseController {
    //TODO: VEDERE SE VA BENE QUESTA IMPLEMENTAZIONE PER LA COMUNICAZIONE CON FRONTEND

    private final PopupNotificationSender popupNotificationSender;

    @Autowired
    public SseController(PopupNotificationSender popupNotificationSender) {
        this.popupNotificationSender = popupNotificationSender;
    }

    @GetMapping(value = "/{userEmail}")
    public SseEmitter handleSse(@PathVariable String userEmail) {
        SseEmitter emitter = new SseEmitter();

        // Registra l'SseEmitter nel PopupNotificationSender
        popupNotificationSender.register(userEmail, emitter);

        // Deregistra l'SseEmitter quando la connessione viene chiusa
        emitter.onCompletion(() -> popupNotificationSender.deregister(userEmail));
        emitter.onTimeout(() -> popupNotificationSender.deregister(userEmail));
        emitter.onError((e) -> popupNotificationSender.deregister(userEmail));

        return emitter;

    }
}
