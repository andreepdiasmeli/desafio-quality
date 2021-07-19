package desafio_quality.dtos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NameValidator implements ConstraintValidator<Named, String> {
    private String className;
    @Override
    public void initialize(Named constraintAnnotation) {
        String given = constraintAnnotation.className();
        if (Objects.nonNull(given)) {
            this.className = given;
        } else {
            this.className = "propriedade";
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(Objects.isNull(value) || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome d" + this.className + " não pode estar vazio.")
                    .addConstraintViolation();
            return false;
        }
        char first = value.charAt(0);
        if(!Character.isLetter(first) || Character.isLowerCase(first)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O nome d" + this.className + " deve começar com uma letra maiúscula.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
