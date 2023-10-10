package be.technobel.ylorth.reservastock_rest.bll.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}
