package desafio_quality.unit.dtos;

import desafio_quality.dtos.UpsertPropertyDTO;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpsertPropertyDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidCreation() {
        UpsertPropertyDTO dto = new UpsertPropertyDTO("Minha Casa", 1L);
        Set<ConstraintViolation<UpsertPropertyDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmptyName() {
        UpsertPropertyDTO dto = new UpsertPropertyDTO("", 1L);
        Set<ConstraintViolation<UpsertPropertyDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome da propriedade não pode estar vazio.")));
    }

    @Test
    public void testInitialLetterName() {
        UpsertPropertyDTO dto = new UpsertPropertyDTO("minha Casa", 1L);
        Set<ConstraintViolation<UpsertPropertyDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome da propriedade deve começar com uma letra maiúscula.")));
    }

    @Test
    public void testLimitSizeName() {
        UpsertPropertyDTO dto = new UpsertPropertyDTO("Minha Casaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 1L);
        Set<ConstraintViolation<UpsertPropertyDTO>> violations = validator.validate(dto);
        assertEquals("O comprimento do nome da propriedade não pode exceder 30 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullDistrictId() {
        UpsertPropertyDTO dto = new UpsertPropertyDTO("Minha Casa", null);
        Set<ConstraintViolation<UpsertPropertyDTO>> violations = validator.validate(dto);
        assertEquals("O id do district não pode estar vazio.", violations.iterator().next().getMessage());
    }


}
