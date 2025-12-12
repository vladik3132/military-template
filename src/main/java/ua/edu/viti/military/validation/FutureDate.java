package ua.edu.viti.military.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FutureDateValidator.class}) // <-- має бути масив
@Documented
public @interface FutureDate {

    String message() default "Дата має бути в майбутньому";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
