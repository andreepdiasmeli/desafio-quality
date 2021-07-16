package desafio_quality.dtos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NameValidator implements ConstraintValidator<Named, String> {
    String fieldName;

    @Override
    public void initialize(Named constraintAnnotation) {
        if (constraintAnnotation.fieldName().equals("None")) {
            fieldName = "";
        } else {
            fieldName = constraintAnnotation.fieldName();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(Objects.isNull(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O campo " + this.fieldName + " não pode ser vazio.")
                    .addConstraintViolation();
            return false;
        }
        if(value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O campo " + this.fieldName + " não pode estar em branco.")
                    .addConstraintViolation();
            return false;
        }
        char first = value.charAt(0);
        if(!Character.isLetter(first) || Character.isLowerCase(first)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "O campo " + this.fieldName + " deve começar com uma letra maiúscula.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
