package be.technobel.ylorth.reservastock_rest.bll.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{

    private final Object innerData;
    public NotFoundException(Object innerData){
        super("Not found");
        this.innerData = innerData;
    }
}
