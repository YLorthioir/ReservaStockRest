package be.technobel.ylorth.reservastock_rest.pl.validation.constraints;

import be.technobel.ylorth.reservastock_rest.pl.validation.validators.EmailNotTakenValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailNotTakenValidator.class)
public @interface EmailNotTaken {
    String message() default "email already taken";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
