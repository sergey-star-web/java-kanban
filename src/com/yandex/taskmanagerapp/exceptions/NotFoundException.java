package com.yandex.taskmanagerapp.exceptions;

import java.io.IOException;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
