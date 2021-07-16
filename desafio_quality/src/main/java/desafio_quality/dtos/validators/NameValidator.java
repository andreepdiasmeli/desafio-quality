package desafio_quality.dtos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NameValidator implements ConstraintValidator<Named, String> {
    @Override
    public void initialize(Named constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(Objects.isNull(value) || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome da propriedade não pode estar vazio.")
                    .addConstraintViolation();
            return false;
        }
        char first = value.charAt(0);
        if(!Character.isLetter(first) || Character.isLowerCase(first)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome da propriedade deve começar com uma letra maiúscula.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
