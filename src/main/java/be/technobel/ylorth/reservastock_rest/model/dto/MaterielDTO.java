package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class MaterielDTO {

    private long id;
    private String nom;
    private Set<SalleDTO> salles = new LinkedHashSet<>();

}
