package it.unisalento.pasproject.notificationservice.exceptions;

import it.unisalento.pasproject.notificationservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends CustomErrorException {

    public NotificationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
