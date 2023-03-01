package be.technobel.ylorth.reservastock_rest.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class SalleDTO {
    private long id;
    private int capacite;
    private String nom;
    private boolean pourPersonnel;
    private Set<DemandeDTO> reserve;

    private Set<MaterielDTO> contient = new LinkedHashSet<>();

}
