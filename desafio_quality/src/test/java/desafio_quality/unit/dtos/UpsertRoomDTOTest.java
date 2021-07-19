package desafio_quality.unit.dtos;

import desafio_quality.dtos.UpsertRoomDTO;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpsertRoomDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidCreation() {
        UpsertRoomDTO dto = new UpsertRoomDTO("Quarto", 5.0, 10.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmptyName() {
        UpsertRoomDTO dto = new UpsertRoomDTO("", 5.0, 10.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome do cômodo não pode estar vazio.")));
    }

    @Test
    public void testInitialLetterName() {
        UpsertRoomDTO dto = new UpsertRoomDTO("quartinho", 5.0, 10.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome do cômodo deve começar com uma letra maiúscula.")));
    }

    @Test
    public void testLimitSizeName() {
        UpsertRoomDTO dto = new UpsertRoomDTO("Quartooooooooooooooooooooooooooooooooo", 5.0, 10.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertEquals("O nome do cômodo não pode exceder 30 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    public void testLowestLimitWidth() {
        UpsertRoomDTO dto = new UpsertRoomDTO("Minha Casa", 0.0, 10.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertEquals("A largura mínima permitida por cômodo é de 1 metro.", violations.iterator().next().getMessage());
    }

    @Test
    public void testHighestLimitWidth() {
        UpsertRoomDTO dto = new UpsertRoomDTO("Minha Casa", 255.0, 10.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertEquals("A largura máxima permitida por cômodo é de 25 metros.", violations.iterator().next().getMessage());
    }

    @Test
    public void testLowestLimitLength() {
        UpsertRoomDTO dto = new UpsertRoomDTO("Minha Casa", 5.0, 0.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertEquals("O comprimento mínimo permitido por cômodo é de 1 metro.", violations.iterator().next().getMessage());
    }

    @Test
    public void testHighestLimitLength() {
        UpsertRoomDTO dto = new UpsertRoomDTO("Minha Casa", 5.0, 333.0);
        Set<ConstraintViolation<UpsertRoomDTO>> violations = validator.validate(dto);
        assertEquals("O comprimento máximo permitido por cômodo é de 33 metros.", violations.iterator().next().getMessage());
    }


}
