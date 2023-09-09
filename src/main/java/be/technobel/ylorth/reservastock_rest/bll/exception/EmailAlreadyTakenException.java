package be.technobel.ylorth.reservastock_rest.bll.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException() {
        super("the specified email is already taken!");
    }
}
