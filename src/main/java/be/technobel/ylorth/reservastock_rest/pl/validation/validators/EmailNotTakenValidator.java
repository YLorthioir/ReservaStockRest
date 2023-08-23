package be.technobel.ylorth.reservastock_rest.pl.validation.validators;

import be.technobel.ylorth.reservastock_rest.bll.service.AuthService;
import be.technobel.ylorth.reservastock_rest.pl.validation.constraints.EmailNotTaken;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class EmailNotTakenValidator implements ConstraintValidator<EmailNotTaken, String> {

    private final AuthService userService;

    public EmailNotTakenValidator(AuthService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userService.checkEmailNotTaken( email );
    }
}
