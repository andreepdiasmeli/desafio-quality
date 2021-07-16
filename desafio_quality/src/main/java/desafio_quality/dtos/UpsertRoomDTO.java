package desafio_quality.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpsertRoomDTO {
    
   // @NotBlank(message = "O campo não pode estar vazio.")
    @Size(min = 1, message = "O nome do cômodo deve conter mais que um caracter.")
    @Size(max = 30, message = "O nome do cômodo não pode exceder 30 caracteres.")
    @Pattern(regexp = "^[A-Z].*$", message = "O nome do cômodo deve começar com uma letra maiúscula.")
    private String name;

    @NotNull(message = "A largura do cômodo não pode estar vazia.")
    @Min(value = 1, message = "A largura mínima permitida por cômodo é de 1 metro.")
    @Max(value = 25, message = "A largura máxima permitida por cômodo é de 25 metros.")
    private double width;

    @NotNull(message = "O comprimento do cômodo não pode estar vazia.")
    @Min(value = 1, message = "O comprimento mínimo permitido por cômodo é de 1 metro.")
    @Max(value = 33, message = "O comprimento máximo permitido por cômodo é de 33 metros.")
    private double length;

    
    public UpsertRoomDTO() {}

    public UpsertRoomDTO(
            String name, 
            double width, 
            double length) {
        this.name = name;
        this.width = width;
        this.length = length;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
