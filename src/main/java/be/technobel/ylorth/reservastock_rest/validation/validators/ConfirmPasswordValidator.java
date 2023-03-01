package be.technobel.ylorth.reservastock_rest.validation.validators;

import be.technobel.ylorth.reservastock_rest.model.form.RegisterForm;
import be.technobel.ylorth.reservastock_rest.validation.constraints.ConfirmPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm value, ConstraintValidatorContext context) {
        return value.getMotDePasse().equals(value.getConfirmMotDePasse());
    }


}
