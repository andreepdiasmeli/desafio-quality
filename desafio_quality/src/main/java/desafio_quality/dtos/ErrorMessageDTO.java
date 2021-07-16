package desafio_quality.dtos;

public class ErrorMessageDTO {
    
    private String error;

    public ErrorMessageDTO() {}

    public ErrorMessageDTO(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }
}
