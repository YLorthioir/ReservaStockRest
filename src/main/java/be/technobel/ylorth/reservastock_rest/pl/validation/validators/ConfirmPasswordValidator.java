package be.technobel.ylorth.reservastock_rest.pl.validation.validators;

import be.technobel.ylorth.reservastock_rest.pl.models.RegisterForm;
import be.technobel.ylorth.reservastock_rest.pl.validation.constraints.ConfirmPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getConfirmPassword());
    }


}
