package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdressDTO {

    private Long id;
    private String number;
    private String street;
    private int postCode;
    private String city;
    private String country;
}
