package sosrota.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import sosrota.application.dtos.UsuarioDTO;
import sosrota.domain.models.PerfilUsuario;

@Component
public class MainViewController {

    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    private Label statusLabel;

    @FXML
    private Menu menuCadastros;

    @FXML
    private Menu menuUtilitarios;

    @FXML
    private VBox cardCadastros;

    @FXML
    private HBox cardUtilitarios;

    private UsuarioDTO usuarioLogado;

    public void setUsuarioLogado(UsuarioDTO usuario) {
        this.usuarioLogado = usuario;
        configurarPermissoes();
    }

    @FXML
    public void initialize() {
        if (statusLabel != null) {
            statusLabel.setText("Status: Sistema Iniciado ✓");
        }
    }

    private void configurarPermissoes() {
        if (usuarioLogado == null) {
            return;
        }

        // REGULADOR: ocultar cadastros e utilitários
        if (usuarioLogado.getPerfil() == PerfilUsuario.REGULADOR) {
            // Ocultar menu Cadastros
            if (menuCadastros != null) {
                menuCadastros.setVisible(false);
            }

            // Ocultar menu Utilitários
            if (menuUtilitarios != null) {
                menuUtilitarios.setVisible(false);
            }

            // Ocultar card Cadastros
            if (cardCadastros != null) {
                cardCadastros.setVisible(false);
                cardCadastros.setManaged(false);
            }

            // Ocultar card Utilitários
            if (cardUtilitarios != null) {
                cardUtilitarios.setVisible(false);
                cardUtilitarios.setManaged(false);
            }

            // Atualizar status
            if (statusLabel != null) {
                statusLabel.setText("Status: Sistema Iniciado ✓ - Perfil: REGULADOR (Acesso Operacional)");
            }
        } else {
            // GESTOR: mostrar tudo
            if (statusLabel != null) {
                statusLabel.setText("Status: Sistema Iniciado ✓ - Perfil: GESTOR (Acesso Completo)");
            }
        }
    }

    @FXML
    private void handleCardClick(MouseEvent event) {
        // Handler genérico para clicks nos cards
    }

    @FXML
    private void abrirCadastroBairros() {
        if (!verificarPermissaoGestor()) return;
        abrirTela("/fxml/BairroView.fxml", "Cadastro de Bairros");
    }

    @FXML
    private void abrirCadastroBases() {
        if (!verificarPermissaoGestor()) return;
        abrirTela("/fxml/BaseView.fxml", "Cadastro de Bases");
    }

    @FXML
    private void abrirCadastroAmbulancias() {
        if (!verificarPermissaoGestor()) return;
        abrirTela("/fxml/AmbulanciaView.fxml", "Cadastro de Ambulâncias");
    }

    @FXML
    private void abrirCadastroProfissionais() {
        if (!verificarPermissaoGestor()) return;
        abrirTela("/fxml/ProfissionalView.fxml", "Cadastro de Profissionais");
    }

    @FXML
    private void abrirCadastroEquipes() {
        if (!verificarPermissaoGestor()) return;
        abrirTela("/fxml/EquipeView.fxml", "Cadastro de Equipes");
    }

    @FXML
    private void abrirCarregarCSV() {
        if (!verificarPermissaoGestor()) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CSVLoaderView.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            CSVLoaderController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStage(stage);
            stage.setTitle("Importar Dados CSV");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            mostrarErro("Erro ao abrir tela de importação: " + e.getMessage());
        }
    }

    private boolean verificarPermissaoGestor() {
        if (usuarioLogado != null && usuarioLogado.getPerfil() == PerfilUsuario.REGULADOR) {
            mostrarErro("Acesso negado! Esta funcionalidade é restrita a usuários GESTOR.");
            return false;
        }
        return true;
    }

    @FXML
    private void abrirOcorrencias() {
        abrirTela("/fxml/OcorrenciaView.fxml", "Cadastro de Ocorrências");
    }

    @FXML
    private void abrirDespacho() {
        abrirTela("/fxml/DespachoView.fxml", "Despacho de Ambulâncias");
    }

    @FXML
    private void abrirRelatorios() {
        abrirTela("/fxml/HistoricoView.fxml", "Histórico de Atendimentos");
    }

    private void abrirTela(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarErro("Erro ao abrir tela: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void mostrarMensagem(String titulo, String mensagem) {
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

