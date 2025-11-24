package sosrota.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import sosrota.application.dtos.LoginDTO;
import sosrota.application.dtos.UsuarioDTO;
import sosrota.application.services.UsuarioService;
import sosrota.ui.controllers.CadastroUsuarioController;

@Component
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Label mensagemLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogin() {
        try {
            mensagemLabel.setVisible(false);
            mensagemLabel.setText("");

            String login = loginField.getText().trim();
            String senha = senhaField.getText();

            if (login.isEmpty() || senha.isEmpty()) {
                mostrarErro("Por favor, preencha login e senha");
                return;
            }

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setLogin(login);
            loginDTO.setSenha(senha);

            UsuarioDTO usuario = usuarioService.autenticar(loginDTO);

            // Login bem-sucedido - abrir tela principal
            abrirTelaPrincipal(usuario);

        } catch (Exception e) {
            mostrarErro("Erro ao fazer login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirTelaPrincipal(UsuarioDTO usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            // Passar usuário logado para o controller
            MainViewController mainController = loader.getController();
            mainController.setUsuarioLogado(usuario);

            Stage mainStage = new Stage();
            mainStage.setTitle("SOS-Rota - Sistema de Gestão de Atendimentos de Emergência - Usuário: " + usuario.getLogin() + " (" + usuario.getPerfil() + ")");
            mainStage.setScene(new Scene(root, 1200, 800));
            mainStage.setMinWidth(1000);
            mainStage.setMinHeight(600);
            mainStage.show();

            // Fechar tela de login
            if (stage != null) {
                stage.close();
            }
        } catch (Exception e) {
            mostrarErro("Erro ao abrir tela principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirCadastroUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CadastroUsuarioView.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            javafx.scene.Parent root = loader.load();

            CadastroUsuarioController controller = loader.getController();
            Stage cadastroStage = new Stage();
            controller.setStage(cadastroStage);
            controller.setLoginController(this);
            
            cadastroStage.setTitle("Cadastrar Novo Usuário - SOS-Rota");
            cadastroStage.setScene(new javafx.scene.Scene(root, 500, 500));
            cadastroStage.setResizable(false);
            cadastroStage.initOwner(stage);
            cadastroStage.show();
        } catch (Exception e) {
            mostrarErro("Erro ao abrir tela de cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        mensagemLabel.setText(mensagem);
        mensagemLabel.setVisible(true);
    }
}

