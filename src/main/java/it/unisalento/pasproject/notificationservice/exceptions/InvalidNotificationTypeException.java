package it.unisalento.pasproject.notificationservice.exceptions;

import it.unisalento.pasproject.notificationservice.exceptions.global.CustomErrorException;
import org.springframework.http.HttpStatus;

public class InvalidNotificationTypeException extends CustomErrorException {
    public InvalidNotificationTypeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
