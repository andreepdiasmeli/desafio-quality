package desafio_quality.dtos;


import desafio_quality.dtos.validators.Named;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class CreateDistrictDTO {

    @Named(className = "o bairro")
    @Size(max=45, message = "O comprimento do nome não pode exceder 45 caracteres.")
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