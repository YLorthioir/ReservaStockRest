package be.technobel.ylorth.reservastock_rest.model.dto;

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

    private Long userId;

    private Long adminId;

    private Long salleId;

    private Set<MaterielDTO> materiels = new LinkedHashSet<>();

}
