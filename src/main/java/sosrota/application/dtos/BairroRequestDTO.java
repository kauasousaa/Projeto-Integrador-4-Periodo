package sosrota.application.dtos;

import jakarta.validation.constraints.NotBlank;

public class BairroRequestDTO {
    @NotBlank
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

