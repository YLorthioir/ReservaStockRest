package be.technobel.ylorth.reservastock_rest.model.form;

import be.technobel.ylorth.reservastock_rest.validation.constraints.OpeningHours;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@OpeningHours
public class RequestForm {
    @NotNull(message = "Entrez un créneau")
    private LocalDateTime starTime;
    @NotNull
    private String requestReason;
    @NotNull
    private int minutes;
    @NotNull
    private Long user;
    @NotNull
    private Long room;

    private Set<Long> materials;

}