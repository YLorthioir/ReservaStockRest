package be.technobel.ylorth.reservastock_rest.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{

    private final Object innerData;
    public NotFoundException(Object innerData){
        super("Not found");
        this.innerData = innerData;
    }
}
