package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdresseDTO {

    private Long id;
    private String numero;
    private String rue;
    private int codePostal;
    private String ville;
    private String pays;
}
