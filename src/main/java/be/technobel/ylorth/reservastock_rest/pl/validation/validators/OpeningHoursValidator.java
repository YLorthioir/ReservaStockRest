package be.technobel.ylorth.reservastock_rest.pl.validation.validators;

import be.technobel.ylorth.reservastock_rest.pl.models.RequestForm;
import be.technobel.ylorth.reservastock_rest.pl.validation.constraints.OpeningHours;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class OpeningHoursValidator implements ConstraintValidator<OpeningHours, RequestForm> {

    private LocalTime opening;
    private LocalTime closing;

    @Override
    public void initialize(OpeningHours constraintAnnotation) {
        this.opening=LocalTime.parse(constraintAnnotation.OpenAt());
        this.closing=LocalTime.parse(constraintAnnotation.CloseAt());
    }

    @Override
    public boolean isValid(RequestForm value, ConstraintValidatorContext context) {
        if(value.getStartTime()!=null)
            return (value.getStartTime().toLocalTime().isAfter(opening) && value.getStartTime().plusMinutes(value.getMinutes()).toLocalTime().isBefore(closing));
        else
            return false;
    }


}
