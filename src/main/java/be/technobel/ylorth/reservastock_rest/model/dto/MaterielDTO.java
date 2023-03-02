package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterielDTO {

    private long id;
    private String nom;
}
