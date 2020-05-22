package de.hso.badenair.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Can't find user: " + username);
    }
}
