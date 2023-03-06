package be.technobel.ylorth.reservastock_rest.validation.validators;

import be.technobel.ylorth.reservastock_rest.model.form.RequestForm;
import be.technobel.ylorth.reservastock_rest.validation.constraints.OpeningHours;
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
        if(value.getStarTime()!=null)
            return (value.getStarTime().toLocalTime().isAfter(opening) && value.getStarTime().plusMinutes(value.getMinutes()).toLocalTime().isBefore(closing));
        else
            return false;
    }


}
