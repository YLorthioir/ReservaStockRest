package be.technobel.ylorth.reservastock_rest.model.dto;

import be.technobel.ylorth.reservastock_rest.model.entity.Salle;
import be.technobel.ylorth.reservastock_rest.model.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class DemandeDTO {
    private long id;
    private LocalDateTime creneau;
    private String raisonDemande;
    private String raisonRefus;
    private int minutes;

    private User user;

    private User admin;

    private Salle salle;

    private Set<MaterielDTO> materiels = new LinkedHashSet<>();

}
