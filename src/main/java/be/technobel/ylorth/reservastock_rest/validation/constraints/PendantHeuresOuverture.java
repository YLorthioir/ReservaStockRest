package be.technobel.ylorth.reservastock_rest.validation.constraints;

import be.technobel.ylorth.reservastock_rest.validation.validators.PendantHeureOuvertureValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PendantHeureOuvertureValidator.class)
public @interface PendantHeuresOuverture {

    String message() default "Demande hors horaire";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
