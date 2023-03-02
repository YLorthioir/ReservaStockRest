package be.technobel.ylorth.reservastock_rest.model.form;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ConfirmForm {

    private String raisonRefus;
    private boolean valide;
    private Long admin;
    private Long salle;


}
