package sosrota.ui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sosrota.application.dtos.UsuarioDTO;
import sosrota.application.dtos.UsuarioRequestDTO;
import sosrota.application.services.UsuarioService;
import sosrota.domain.models.PerfilUsuario;

@Component
public class CadastroUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private PasswordField confirmarSenhaField;

    @FXML
    private ComboBox<String> perfilComboBox;

    @FXML
    private Label mensagemLabel;

    private Stage stage;
    private LoginController loginController;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @FXML
    public void initialize() {
        perfilComboBox.setItems(FXCollections.observableArrayList("REGULADOR", "GESTOR"));
        perfilComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleCadastrar() {
        try {
            mensagemLabel.setVisible(false);
            mensagemLabel.setText("");

            String login = loginField.getText().trim();
            String senha = senhaField.getText();
            String confirmarSenha = confirmarSenhaField.getText();
            String perfilStr = perfilComboBox.getSelectionModel().getSelectedItem();

            // Validações
            if (login.isEmpty()) {
                mostrarErro("Por favor, preencha o login");
                return;
            }

            if (senha.isEmpty()) {
                mostrarErro("Por favor, preencha a senha");
                return;
            }

            if (senha.length() < 3) {
                mostrarErro("A senha deve ter pelo menos 3 caracteres");
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                mostrarErro("As senhas não coincidem");
                return;
            }

            if (perfilStr == null || perfilStr.isEmpty()) {
                mostrarErro("Por favor, selecione um perfil");
                return;
            }

            // Converter string para enum
            PerfilUsuario perfil;
            try {
                perfil = PerfilUsuario.valueOf(perfilStr);
            } catch (IllegalArgumentException e) {
                mostrarErro("Perfil inválido");
                return;
            }

            // Criar usuário
            UsuarioRequestDTO request = new UsuarioRequestDTO();
            request.setLogin(login);
            request.setSenha(senha);
            request.setPerfil(perfil);

            UsuarioDTO usuario = usuarioService.criar(request);

            // Sucesso - mostrar mensagem e fechar
            mostrarSucesso("Usuário cadastrado com sucesso! Login: " + usuario.getLogin());
            
            // Limpar campos
            loginField.clear();
            senhaField.clear();
            confirmarSenhaField.clear();
            perfilComboBox.getSelectionModel().selectFirst();

            // Fechar após 2 segundos ou permitir fechar manualmente
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        if (stage != null) {
                            stage.close();
                        }
                    });
                } catch (InterruptedException e) {
                    // Ignorar
                }
            }).start();

        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        if (stage != null) {
            stage.close();
        }
    }

    private void mostrarErro(String mensagem) {
        mensagemLabel.setStyle("-fx-text-fill: #e74c3c;");
        mensagemLabel.setText(mensagem);
        mensagemLabel.setVisible(true);
    }

    private void mostrarSucesso(String mensagem) {
        mensagemLabel.setStyle("-fx-text-fill: #27ae60;");
        mensagemLabel.setText(mensagem);
        mensagemLabel.setVisible(true);
    }
}

