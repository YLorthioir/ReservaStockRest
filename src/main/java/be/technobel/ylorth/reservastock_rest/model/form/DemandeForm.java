package be.technobel.ylorth.reservastock_rest.model.form;

import be.technobel.ylorth.reservastock_rest.validation.constraints.PendantHeuresOuverture;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@PendantHeuresOuverture
public class DemandeForm {
    @NotNull(message = "Entrez un créneau")
    private LocalDateTime creneau;
    @NotNull
    private String raisonDemande;
    @NotNull
    private int minutes;
    @NotNull
    private Long user;
    @NotNull
    private Long salle;

    private Set<Long> materiels;

}
