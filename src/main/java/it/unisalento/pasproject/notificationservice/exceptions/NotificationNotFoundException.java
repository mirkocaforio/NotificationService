package it.unisalento.pasproject.notificationservice.exceptions;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends CustomErrorException {

    public NotificationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
