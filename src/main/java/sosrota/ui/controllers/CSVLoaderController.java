package sosrota.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sosrota.application.services.CSVLoaderService;

import java.io.File;

@Component
public class CSVLoaderController {

    @Autowired
    private CSVLoaderService csvLoaderService;

    @FXML
    private TextField bairrosPathField;

    @FXML
    private TextField conexoesPathField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void selecionarBairros() {
        File arquivo = selecionarArquivo("Selecionar arquivo de Bairros", "*.csv");
        if (arquivo != null) {
            bairrosPathField.setText(arquivo.getAbsolutePath());
        }
    }

    @FXML
    private void selecionarConexoes() {
        File arquivo = selecionarArquivo("Selecionar arquivo de Conexões", "*.csv");
        if (arquivo != null) {
            conexoesPathField.setText(arquivo.getAbsolutePath());
        }
    }

    @FXML
    private void importarTudo() {
        String caminhoBairros = bairrosPathField.getText().trim();
        String caminhoConexoes = conexoesPathField.getText().trim();

        if (caminhoBairros.isEmpty() || caminhoConexoes.isEmpty()) {
            mostrarErro("Por favor, selecione ambos os arquivos CSV.");
            return;
        }

        try {
            csvLoaderService.carregarTodos(caminhoBairros, caminhoConexoes, msg -> {});
            mostrarSucesso("Importação concluída", "Os dados foram importados com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao importar: " + e.getMessage());
        }
    }

    @FXML
    private void fechar() {
        if (stage != null) {
            stage.close();
        }
    }

    private File selecionarArquivo(String titulo, String extensao) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titulo);
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Arquivos CSV", extensao)
        );
        return fileChooser.showOpenDialog(stage);
    }

    private void mostrarSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}



