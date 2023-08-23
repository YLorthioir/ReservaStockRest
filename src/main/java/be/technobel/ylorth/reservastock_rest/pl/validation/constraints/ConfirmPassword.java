package be.technobel.ylorth.reservastock_rest.pl.validation.constraints;

import be.technobel.ylorth.reservastock_rest.pl.validation.validators.ConfirmPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmPasswordValidator.class)
public @interface ConfirmPassword {

    String message() default "Password isn\'t the same";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
