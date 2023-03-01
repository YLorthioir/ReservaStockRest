package be.technobel.ylorth.reservastock_rest.validation.validators;

import be.technobel.ylorth.reservastock_rest.service.UserService;
import be.technobel.ylorth.reservastock_rest.validation.constraints.EmailNotTaken;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class EmailNotTakenValidator implements ConstraintValidator<EmailNotTaken, String> {

    private final UserService userService;

    public EmailNotTakenValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userService.checkEmailNotTaken( email );
    }
}
