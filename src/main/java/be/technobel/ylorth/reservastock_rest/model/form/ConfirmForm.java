package be.technobel.ylorth.reservastock_rest.model.form;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ConfirmForm {

    private LocalDateTime creneau;
    private int minutes;
    private String raisonDemande;
    private String raisonRefus;
    private Long user;
    private boolean valide;
    private Long admin;
    private Long salle;

    private Set<Long> materiels = new LinkedHashSet<>();

}
