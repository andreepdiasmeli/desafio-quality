package desafio_quality.unit.dtos;

import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.PropertyDTO;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateDistrictDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidCreation() {
        CreateDistrictDTO dto = new CreateDistrictDTO("Costa e Silva", new BigDecimal("8000"));
        Set<ConstraintViolation<CreateDistrictDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmptyName() {
        CreateDistrictDTO dto = new CreateDistrictDTO("", new BigDecimal("8000"));
        Set<ConstraintViolation<CreateDistrictDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome do bairro não pode estar vazio.")));
    }

    @Test
    public void testInitialLetterName() {
        CreateDistrictDTO dto = new CreateDistrictDTO("costa e Silva", new BigDecimal("80000"));
        Set<ConstraintViolation<CreateDistrictDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome do bairro deve começar com uma letra maiúscula.")));
    }

    @Test
    public void testLimitSizeName() {
        CreateDistrictDTO dto = new CreateDistrictDTO("Costa e Silvaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", new BigDecimal("8000"));
        Set<ConstraintViolation<CreateDistrictDTO>> violations = validator.validate(dto);
        assertEquals("O nome do bairro não pode exceder 45 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullSquareMetterValue() {
        CreateDistrictDTO dto = new CreateDistrictDTO("Costa e Silva", null);
        Set<ConstraintViolation<CreateDistrictDTO>> violations = validator.validate(dto);
        assertEquals("O valor do metro quadrado não pode estar vazio.", violations.iterator().next().getMessage());
    }

    @Test
    public void testLimitDigitsSquareMetterValue() {
        CreateDistrictDTO dto = new CreateDistrictDTO("Costa e Silva", new BigDecimal("123456789123456"));
        Set<ConstraintViolation<CreateDistrictDTO>> violations = validator.validate(dto);
        assertEquals("O valor de metros quadrados não deve exceder 13 digitos.", violations.iterator().next().getMessage());
    }



}
