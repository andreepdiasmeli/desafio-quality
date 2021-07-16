package desafio_quality.dtos;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Valid
public class UpsertPropertyDTO {
   // @NotNull(message = "O nome da propriedade não pode ser vazio.")
   // @Size(min = 1, message = "O nome da propriedade não pode ser vazio.")
    //@Size(min = 1, message = "O nome da propriedade não pode ser vazio.")
    @Size(max = 30, message = "O comprimento do nome da propriedade não pode exceder 30 caracteres.")
    @Pattern(regexp = "^[A-Z].*$", message = "O nome do cômodo deve começar com uma letra maiúscula.")
    private String name;

    @NotNull
    private Long districtId;

    public UpsertPropertyDTO() {
    }

    public UpsertPropertyDTO(String name, Long districtId) {
        this.name = name;
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
}
