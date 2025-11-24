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
import sosrota.application.dtos.AmbulanciaDTO;
import sosrota.application.dtos.AmbulanciaRequestDTO;
import sosrota.application.dtos.BaseDTO;
import sosrota.application.services.AmbulanciaService;
import sosrota.application.services.BaseService;
import sosrota.domain.models.StatusAmbulancia;
import sosrota.domain.models.TipoAmbulancia;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AmbulanciaController implements Initializable {

    @Autowired
    private AmbulanciaService ambulanciaService;

    @Autowired
    private BaseService baseService;

    @FXML
    private TextField placaField;

    @FXML
    private ComboBox<TipoAmbulancia> tipoComboBox;

    @FXML
    private ComboBox<BaseDTO> baseComboBox;

    @FXML
    private ComboBox<StatusAmbulancia> statusComboBox;

    @FXML
    private TableView<AmbulanciaDTO> tableView;

    @FXML
    private TableColumn<AmbulanciaDTO, Long> idColumn;

    @FXML
    private TableColumn<AmbulanciaDTO, String> placaColumn;

    @FXML
    private TableColumn<AmbulanciaDTO, TipoAmbulancia> tipoColumn;

    @FXML
    private TableColumn<AmbulanciaDTO, StatusAmbulancia> statusColumn;

    @FXML
    private TableColumn<AmbulanciaDTO, String> baseColumn;

    private ObservableList<AmbulanciaDTO> ambulanciasList;
    private ObservableList<BaseDTO> basesList;
    private AmbulanciaDTO ambulanciaSelecionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        placaColumn.setCellValueFactory(new PropertyValueFactory<>("placa"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        baseColumn.setCellValueFactory(new PropertyValueFactory<>("baseNome"));

        ambulanciasList = FXCollections.observableArrayList();
        basesList = FXCollections.observableArrayList();
        tableView.setItems(ambulanciasList);
        baseComboBox.setItems(basesList);
        tipoComboBox.getItems().addAll(TipoAmbulancia.values());
        statusComboBox.getItems().addAll(StatusAmbulancia.values());

        baseComboBox.setConverter(new javafx.util.StringConverter<BaseDTO>() {
            @Override
            public String toString(BaseDTO base) {
                return base == null ? "" : base.getNome();
            }

            @Override
            public BaseDTO fromString(String string) {
                return basesList.stream().filter(b -> b.getNome().equals(string)).findFirst().orElse(null);
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            ambulanciaSelecionada = newSelection;
            if (newSelection != null) {
                carregarDadosFormulario(newSelection);
            }
        });

        carregarBases();
        carregarDados();
    }

    @FXML
    private void novo() {
        limparFormulario();
        ambulanciaSelecionada = null;
        tableView.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().select(StatusAmbulancia.DISPONIVEL);
    }

    @FXML
    private void salvar() {
        try {
            if (placaField.getText().trim().isEmpty()) {
                mostrarErro("Placa é obrigatória!");
                return;
            }
            if (tipoComboBox.getSelectionModel().getSelectedItem() == null) {
                mostrarErro("Tipo é obrigatório!");
                return;
            }
            if (baseComboBox.getSelectionModel().getSelectedItem() == null) {
                mostrarErro("Base é obrigatória!");
                return;
            }

            AmbulanciaRequestDTO request = new AmbulanciaRequestDTO();
            request.setPlaca(placaField.getText().trim().toUpperCase());
            request.setTipo(tipoComboBox.getSelectionModel().getSelectedItem());
            request.setBaseId(baseComboBox.getSelectionModel().getSelectedItem().getId());

            if (ambulanciaSelecionada == null) {
                ambulanciaService.criar(request);
                mostrarSucesso("Ambulância criada com sucesso!");
            } else {
                ambulanciaService.atualizar(ambulanciaSelecionada.getId(), request);
                if (statusComboBox.getSelectionModel().getSelectedItem() != null) {
                    ambulanciaService.atualizarStatus(ambulanciaSelecionada.getId(),
                            statusComboBox.getSelectionModel().getSelectedItem().name());
                }
                mostrarSucesso("Ambulância atualizada com sucesso!");
            }

            limparFormulario();
            carregarDados();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void editar() {
        if (ambulanciaSelecionada == null) {
            mostrarErro("Selecione uma ambulância para editar!");
            return;
        }
        carregarDadosFormulario(ambulanciaSelecionada);
    }

    @FXML
    private void excluir() {
        if (ambulanciaSelecionada == null) {
            mostrarErro("Selecione uma ambulância para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir a ambulância: " + ambulanciaSelecionada.getPlaca() + "?");
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ambulanciaService.deletar(ambulanciaSelecionada.getId());
                    mostrarSucesso("Ambulância excluída com sucesso!");
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
        ambulanciaSelecionada = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) placaField.getScene().getWindow();
        stage.close();
    }

    private void carregarDados() {
        try {
            ambulanciasList.clear();
            ambulanciasList.addAll(ambulanciaService.listarTodas());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void carregarBases() {
        try {
            basesList.clear();
            basesList.addAll(baseService.listarTodas());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar bases: " + e.getMessage());
        }
    }

    private void carregarDadosFormulario(AmbulanciaDTO ambulancia) {
        placaField.setText(ambulancia.getPlaca());
        tipoComboBox.getSelectionModel().select(ambulancia.getTipo());
        statusComboBox.getSelectionModel().select(ambulancia.getStatus());
        BaseDTO base = basesList.stream()
                .filter(b -> b.getId().equals(ambulancia.getBaseId()))
                .findFirst()
                .orElse(null);
        baseComboBox.getSelectionModel().select(base);
    }

    private void limparFormulario() {
        placaField.clear();
        tipoComboBox.getSelectionModel().clearSelection();
        baseComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
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



