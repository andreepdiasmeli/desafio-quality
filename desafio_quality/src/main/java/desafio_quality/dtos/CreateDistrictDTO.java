package desafio_quality.dtos;


import javax.validation.constraints.*;
import java.math.BigDecimal;

public class CreateDistrictDTO {

    @Size(max = 45, message="O nome do bairro não pode exceder 45 caracteres.")
    @Pattern(regexp = "^[A-Z].*$", message = "O nome do bairro deve começar com uma letra maiúscula.")
    //@NotBlank(message = "O campo não pode estar vazio.")
    private String name;

    @NotNull(message = "O valor do metro quadrado não pode estar vazio.")
    @Digits(integer=13, fraction = 2, message = "O valor de metros quadrados não deve exceder 13 digitos.")
    private BigDecimal squareMeterValue;

    public CreateDistrictDTO() {}

    public CreateDistrictDTO(String name, BigDecimal squareMeterValue) {
        this.name = name;
        this.squareMeterValue = squareMeterValue;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSquareMeterValue() {
        return squareMeterValue;
    }
}
