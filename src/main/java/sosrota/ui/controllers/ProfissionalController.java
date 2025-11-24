package sosrota.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sosrota.application.dtos.ProfissionalDTO;
import sosrota.application.dtos.ProfissionalRequestDTO;
import sosrota.application.services.ProfissionalService;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ProfissionalController implements Initializable {

    @Autowired
    private ProfissionalService profissionalService;

    @FXML
    private TextField nomeField;

    @FXML
    private ComboBox<String> funcaoComboBox;

    @FXML
    private TextField contatoField;

    @FXML
    private TableView<ProfissionalDTO> tableView;

    @FXML
    private TableColumn<ProfissionalDTO, Long> idColumn;

    @FXML
    private TableColumn<ProfissionalDTO, String> nomeColumn;

    @FXML
    private TableColumn<ProfissionalDTO, String> funcaoColumn;

    @FXML
    private TableColumn<ProfissionalDTO, String> contatoColumn;

    @FXML
    private TableColumn<ProfissionalDTO, Boolean> ativoColumn;

    private ObservableList<ProfissionalDTO> profissionaisList;
    private ProfissionalDTO profissionalSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        funcaoColumn.setCellValueFactory(new PropertyValueFactory<>("funcao"));
        contatoColumn.setCellValueFactory(new PropertyValueFactory<>("contato"));
        ativoColumn.setCellValueFactory(new PropertyValueFactory<>("ativo"));

        profissionaisList = FXCollections.observableArrayList();
        tableView.setItems(profissionaisList);

        funcaoComboBox.getItems().addAll("Médico", "Enfermeiro", "Condutor", "Técnico");

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            profissionalSelecionado = newSelection;
            if (newSelection != null) {
                carregarDadosFormulario(newSelection);
            }
        });

        carregarDados();
    }

    @FXML
    private void novo() {
        limparFormulario();
        profissionalSelecionado = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void salvar() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                mostrarErro("Nome é obrigatório!");
                return;
            }
            if (funcaoComboBox.getSelectionModel().getSelectedItem() == null) {
                mostrarErro("Função é obrigatória!");
                return;
            }
            if (contatoField.getText().trim().isEmpty()) {
                mostrarErro("Contato é obrigatório!");
                return;
            }

            ProfissionalRequestDTO request = new ProfissionalRequestDTO();
            request.setNome(nomeField.getText().trim());
            request.setFuncao(funcaoComboBox.getSelectionModel().getSelectedItem());
            request.setContato(contatoField.getText().trim());

            if (profissionalSelecionado == null) {
                profissionalService.criar(request);
                mostrarSucesso("Profissional criado com sucesso!");
            } else {
                profissionalService.atualizar(profissionalSelecionado.getId(), request);
                mostrarSucesso("Profissional atualizado com sucesso!");
            }

            limparFormulario();
            carregarDados();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void editar() {
        if (profissionalSelecionado == null) {
            mostrarErro("Selecione um profissional para editar!");
            return;
        }
        carregarDadosFormulario(profissionalSelecionado);
    }

    @FXML
    private void excluir() {
        if (profissionalSelecionado == null) {
            mostrarErro("Selecione um profissional para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir o profissional: " + profissionalSelecionado.getNome() + "?");
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    profissionalService.deletar(profissionalSelecionado.getId());
                    mostrarSucesso("Profissional excluído com sucesso!");
                    limparFormulario();
                    carregarDados();
                } catch (Exception e) {
                    mostrarErro("Erro ao excluir: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void cancelar() {
        limparFormulario();
        profissionalSelecionado = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    private void carregarDados() {
        try {
            profissionaisList.clear();
            profissionaisList.addAll(profissionalService.listarTodas());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void carregarDadosFormulario(ProfissionalDTO profissional) {
        nomeField.setText(profissional.getNome());
        funcaoComboBox.getSelectionModel().select(profissional.getFuncao());
        contatoField.setText(profissional.getContato());
    }

    private void limparFormulario() {
        nomeField.clear();
        funcaoComboBox.getSelectionModel().clearSelection();
        contatoField.clear();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
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



