package sosrota.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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

    @FXML
    private TextArea logArea;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void selecionarBairros() {
        File arquivo = selecionarArquivo("Selecionar arquivo de Bairros", "*.csv");
        if (arquivo != null) {
            bairrosPathField.setText(arquivo.getAbsolutePath());
            adicionarLog("Arquivo de bairros selecionado: " + arquivo.getName());
        }
    }

    @FXML
    private void selecionarConexoes() {
        File arquivo = selecionarArquivo("Selecionar arquivo de Conexões", "*.csv");
        if (arquivo != null) {
            conexoesPathField.setText(arquivo.getAbsolutePath());
            adicionarLog("Arquivo de conexões selecionado: " + arquivo.getName());
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
            adicionarLog("Iniciando importação completa...");
            csvLoaderService.carregarTodos(caminhoBairros, caminhoConexoes);
            adicionarLog("✓ Importação concluída com sucesso!");
            mostrarSucesso("Importação concluída", "Os dados foram importados com sucesso!");
        } catch (Exception e) {
            adicionarLog("✗ Erro: " + e.getMessage());
            mostrarErro("Erro ao importar: " + e.getMessage());
        }
    }

    @FXML
    private void importarBairros() {
        String caminho = bairrosPathField.getText().trim();
        if (caminho.isEmpty()) {
            mostrarErro("Por favor, selecione o arquivo de bairros.");
            return;
        }

        try {
            adicionarLog("Importando bairros...");
            csvLoaderService.carregarBairros(caminho);
            adicionarLog("✓ Bairros importados com sucesso!");
            mostrarSucesso("Importação concluída", "Os bairros foram importados com sucesso!");
        } catch (Exception e) {
            adicionarLog("✗ Erro: " + e.getMessage());
            mostrarErro("Erro ao importar bairros: " + e.getMessage());
        }
    }

    @FXML
    private void importarConexoes() {
        String caminho = conexoesPathField.getText().trim();
        if (caminho.isEmpty()) {
            mostrarErro("Por favor, selecione o arquivo de conexões.");
            return;
        }

        try {
            adicionarLog("Importando conexões...");
            csvLoaderService.carregarConexoes(caminho);
            adicionarLog("✓ Conexões importadas com sucesso!");
            mostrarSucesso("Importação concluída", "As conexões foram importadas com sucesso!");
        } catch (Exception e) {
            adicionarLog("✗ Erro: " + e.getMessage());
            mostrarErro("Erro ao importar conexões: " + e.getMessage());
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

    private void adicionarLog(String mensagem) {
        if (logArea != null) {
            logArea.appendText(mensagem + "\n");
        }
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

