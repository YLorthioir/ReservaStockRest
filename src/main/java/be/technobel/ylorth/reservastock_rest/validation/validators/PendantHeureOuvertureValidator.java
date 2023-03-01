package be.technobel.ylorth.reservastock_rest.validation.validators;

import be.technobel.ylorth.reservastock_rest.model.form.DemandeForm;
import be.technobel.ylorth.reservastock_rest.validation.constraints.PendantHeuresOuverture;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class PendantHeureOuvertureValidator implements ConstraintValidator<PendantHeuresOuverture, DemandeForm> {

    @Override
    public boolean isValid(DemandeForm value, ConstraintValidatorContext context) {
        if(value.getCreneau()!=null)
            return (value.getCreneau().toLocalTime().isAfter(LocalTime.of(8,0)) && value.getCreneau().plusMinutes(value.getMinutes()).toLocalTime().isBefore(LocalTime.of(18,00)));
        else
            return false;
    }


}
