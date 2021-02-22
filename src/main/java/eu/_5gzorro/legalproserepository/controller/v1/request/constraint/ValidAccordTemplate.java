package eu._5gzorro.legalproserepository.controller.v1.request.constraint;

import eu._5gzorro.legalproserepository.controller.v1.request.validator.LegalProseTemplateFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LegalProseTemplateFileValidator.class)
public @interface ValidAccordTemplate {
    String message() default "{eu._5gzorro.legalproserepository.ValidAccordTemplate.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}