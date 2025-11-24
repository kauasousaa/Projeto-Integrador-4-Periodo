package sosrota.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sosrota.application.dtos.BaseDTO;
import sosrota.application.dtos.BaseRequestDTO;
import sosrota.application.dtos.BairroDTO;
import sosrota.application.services.BaseService;
import sosrota.application.services.BairroService;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class BaseController implements Initializable {

    @Autowired
    private BaseService baseService;

    @Autowired
    private BairroService bairroService;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField enderecoField;

    @FXML
    private ComboBox<BairroDTO> bairroComboBox;

    @FXML
    private TableView<BaseDTO> tableView;

    @FXML
    private TableColumn<BaseDTO, Long> idColumn;

    @FXML
    private TableColumn<BaseDTO, String> nomeColumn;

    @FXML
    private TableColumn<BaseDTO, String> enderecoColumn;

    @FXML
    private TableColumn<BaseDTO, String> bairroColumn;

    private ObservableList<BaseDTO> basesList;
    private ObservableList<BairroDTO> bairrosList;
    private BaseDTO baseSelecionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        bairroColumn.setCellValueFactory(new PropertyValueFactory<>("bairroNome"));

        basesList = FXCollections.observableArrayList();
        bairrosList = FXCollections.observableArrayList();
        tableView.setItems(basesList);
        bairroComboBox.setItems(bairrosList);

        bairroComboBox.setConverter(new StringConverter<BairroDTO>() {
            @Override
            public String toString(BairroDTO bairro) {
                return bairro == null ? "" : bairro.getNome();
            }

            @Override
            public BairroDTO fromString(String string) {
                return bairrosList.stream()
                        .filter(b -> b.getNome().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            baseSelecionada = newSelection;
            if (newSelection != null) {
                carregarDadosFormulario(newSelection);
            }
        });

        carregarBairros();
        carregarDados();
    }

    @FXML
    private void novo() {
        limparFormulario();
        baseSelecionada = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void salvar() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                mostrarErro("Nome é obrigatório!");
                return;
            }
            if (enderecoField.getText().trim().isEmpty()) {
                mostrarErro("Endereço é obrigatório!");
                return;
            }
            if (bairroComboBox.getSelectionModel().getSelectedItem() == null) {
                mostrarErro("Bairro é obrigatório!");
                return;
            }

            BaseRequestDTO request = new BaseRequestDTO();
            request.setNome(nomeField.getText().trim());
            request.setEndereco(enderecoField.getText().trim());
            request.setBairroId(bairroComboBox.getSelectionModel().getSelectedItem().getId());

            if (baseSelecionada == null) {
                baseService.criar(request);
                mostrarSucesso("Base criada com sucesso!");
            } else {
                baseService.atualizar(baseSelecionada.getId(), request);
                mostrarSucesso("Base atualizada com sucesso!");
            }

            limparFormulario();
            carregarDados();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void editar() {
        if (baseSelecionada == null) {
            mostrarErro("Selecione uma base para editar!");
            return;
        }
        carregarDadosFormulario(baseSelecionada);
    }

    @FXML
    private void excluir() {
        if (baseSelecionada == null) {
            mostrarErro("Selecione uma base para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir a base: " + baseSelecionada.getNome() + "?");
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    baseService.deletar(baseSelecionada.getId());
                    mostrarSucesso("Base excluída com sucesso!");
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
        baseSelecionada = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    private void carregarDados() {
        try {
            basesList.clear();
            basesList.addAll(baseService.listarTodas());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void carregarBairros() {
        try {
            bairrosList.clear();
            bairrosList.addAll(bairroService.listarTodos());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar bairros: " + e.getMessage());
        }
    }

    private void carregarDadosFormulario(BaseDTO base) {
        nomeField.setText(base.getNome());
        enderecoField.setText(base.getEndereco());
        BairroDTO bairro = bairrosList.stream()
                .filter(b -> b.getId().equals(base.getBairroId()))
                .findFirst()
                .orElse(null);
        bairroComboBox.getSelectionModel().select(bairro);
    }

    private void limparFormulario() {
        nomeField.clear();
        enderecoField.clear();
        bairroComboBox.getSelectionModel().clearSelection();
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



