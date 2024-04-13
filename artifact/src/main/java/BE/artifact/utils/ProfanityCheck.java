package BE.artifact.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProfanityValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfanityCheck {
    String message() default "Inappropriate language is not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}