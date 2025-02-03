package Exceptions;

public class AbonnementAlreadyExistsException extends RuntimeException {
    public AbonnementAlreadyExistsException(String message) {
        super(message);
    }
}
