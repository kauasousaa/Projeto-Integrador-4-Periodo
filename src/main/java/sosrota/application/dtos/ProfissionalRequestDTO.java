package sosrota.application.dtos;

import jakarta.validation.constraints.NotBlank;

public class ProfissionalRequestDTO {
    @NotBlank
    private String nome;

    @NotBlank
    private String funcao;

    @NotBlank
    private String contato;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}

