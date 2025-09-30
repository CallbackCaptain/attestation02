package com.example.dungeon.core;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException() {
        super("Это не команда! Используй 'помощь' если не шаришь.");
    }
}
