package be.technobel.ylorth.reservastock_rest.exception;

import lombok.Getter;

@Getter
public class RoomNotFreeException extends RuntimeException{

    private final Object innerData;
    public RoomNotFreeException(Object innerData){
        super("Free room not found");
        this.innerData = innerData;
    }
}
