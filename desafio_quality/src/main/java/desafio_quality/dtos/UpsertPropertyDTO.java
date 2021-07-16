package desafio_quality.dtos;

import desafio_quality.dtos.validators.Named;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
public class UpsertPropertyDTO {
    @Named(fieldName = "nome da propriedade")
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
