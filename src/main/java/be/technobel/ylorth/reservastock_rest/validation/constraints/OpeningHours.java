package be.technobel.ylorth.reservastock_rest.validation.constraints;

import be.technobel.ylorth.reservastock_rest.validation.validators.OpeningHoursValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OpeningHoursValidator.class)
public @interface OpeningHours {

    String message() default "The request must be in the opening hours";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
