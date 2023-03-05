package be.technobel.ylorth.reservastock_rest.validation.validators;

import be.technobel.ylorth.reservastock_rest.model.form.RequestForm;
import be.technobel.ylorth.reservastock_rest.validation.constraints.OpeningHours;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class OpeningHoursValidator implements ConstraintValidator<OpeningHours, RequestForm> {

    @Override
    public boolean isValid(RequestForm value, ConstraintValidatorContext context) {
        if(value.getStarTime()!=null)
            return (value.getStarTime().toLocalTime().isAfter(LocalTime.of(8,0)) && value.getStarTime().plusMinutes(value.getMinutes()).toLocalTime().isBefore(LocalTime.of(18,00)));
        else
            return false;
    }


}
