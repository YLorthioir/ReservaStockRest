package be.technobel.ylorth.reservastock_rest.model.form;

import be.technobel.ylorth.reservastock_rest.model.dto.DemandeDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class SalleForm {

    @NotNull
    private int capacite;
    @NotNull
    @Size(min = 4, max = 40)
    private String nom;

    boolean pourPersonnel;
    private Set<DemandeDTO> reserve;

    private Set<Long> contient = new LinkedHashSet<>();

}
