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
import sosrota.application.dtos.BairroDTO;
import sosrota.application.dtos.BairroRequestDTO;
import sosrota.application.services.BairroService;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class BairroController implements Initializable {

    @Autowired
    private BairroService bairroService;

    @FXML
    private TextField nomeField;

    @FXML
    private TableView<BairroDTO> tableView;

    @FXML
    private TableColumn<BairroDTO, Long> idColumn;

    @FXML
    private TableColumn<BairroDTO, String> nomeColumn;

    private ObservableList<BairroDTO> bairrosList;
    private BairroDTO bairroSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        bairrosList = FXCollections.observableArrayList();
        tableView.setItems(bairrosList);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            bairroSelecionado = newSelection;
            if (newSelection != null) {
                carregarDadosFormulario(newSelection);
            }
        });

        carregarDados();
    }

    @FXML
    private void novo() {
        limparFormulario();
        bairroSelecionado = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void salvar() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                mostrarErro("Nome é obrigatório!");
                return;
            }

            BairroRequestDTO request = new BairroRequestDTO();
            request.setNome(nomeField.getText().trim());

            if (bairroSelecionado == null) {
                // Criar novo
                bairroService.criar(request);
                mostrarSucesso("Bairro criado com sucesso!");
            } else {
                // Atualizar existente
                bairroService.atualizar(bairroSelecionado.getId(), request);
                mostrarSucesso("Bairro atualizado com sucesso!");
            }

            limparFormulario();
            carregarDados();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void editar() {
        if (bairroSelecionado == null) {
            mostrarErro("Selecione um bairro para editar!");
            return;
        }
        carregarDadosFormulario(bairroSelecionado);
    }

    @FXML
    private void excluir() {
        if (bairroSelecionado == null) {
            mostrarErro("Selecione um bairro para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir o bairro: " + bairroSelecionado.getNome() + "?");
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    bairroService.deletar(bairroSelecionado.getId());
                    mostrarSucesso("Bairro excluído com sucesso!");
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
        bairroSelecionado = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    private void carregarDados() {
        try {
            bairrosList.clear();
            bairrosList.addAll(bairroService.listarTodos());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void carregarDadosFormulario(BairroDTO bairro) {
        nomeField.setText(bairro.getNome());
    }

    private void limparFormulario() {
        nomeField.clear();
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



