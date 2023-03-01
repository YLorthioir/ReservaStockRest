package be.technobel.ylorth.reservastock_rest.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException() {
        super("the specified email is already taken");
    }
}
