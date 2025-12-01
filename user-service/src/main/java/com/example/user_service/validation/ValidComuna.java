package com.example.user_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ComunaValidator.class)
@Target({ ElementType.TYPE }) // importante: validamos todo el objeto (DTO) porque necesitamos región y comuna
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidComuna {
    String message() default "La comuna no pertenece a la región seleccionada";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
