package be.technobel.ylorth.reservastock_rest.exception;

import lombok.Getter;

@Getter
public class PasDeLocalDispoException extends RuntimeException{

    private final Object innerData;
    public PasDeLocalDispoException(Object innerData){
        super("Not found");
        this.innerData = innerData;
    }
}
