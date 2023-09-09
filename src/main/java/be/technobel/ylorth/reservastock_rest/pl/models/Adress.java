package be.technobel.ylorth.reservastock_rest.pl.models;

import be.technobel.ylorth.reservastock_rest.dal.models.AdressEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Adress {

    private Long id;
    private String number;
    private String street;
    private int postCode;
    private String city;
    private String country;

    public static Adress fromBLL(AdressEntity entity){

        if(entity == null)
            return null;

        return Adress.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .street(entity.getStreet())
                .city(entity.getCity())
                .postCode(entity.getPostCode())
                .country(entity.getCountry())
                .build();
    }
}
