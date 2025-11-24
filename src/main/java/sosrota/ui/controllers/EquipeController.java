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
import sosrota.application.dtos.EquipeDTO;
import sosrota.application.dtos.EquipeRequestDTO;
import sosrota.application.dtos.ProfissionalDTO;
import sosrota.application.dtos.AmbulanciaDTO;
import sosrota.application.services.EquipeService;
import sosrota.application.services.AmbulanciaService;
import sosrota.application.services.ProfissionalService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class EquipeController implements Initializable {

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private AmbulanciaService ambulanciaService;

    @Autowired
    private ProfissionalService profissionalService;

    @FXML
    private TextField descricaoField;

    @FXML
    private ComboBox<AmbulanciaDTO> ambulanciaComboBox;

    @FXML
    private ListView<ProfissionalDTO> profissionaisListView;

    @FXML
    private ComboBox<ProfissionalDTO> profissionalComboBox;

    @FXML
    private TableView<EquipeDTO> tableView;

    @FXML
    private TableColumn<EquipeDTO, Long> idColumn;

    @FXML
    private TableColumn<EquipeDTO, String> descricaoColumn;

    @FXML
    private TableColumn<EquipeDTO, Long> ambulanciaIdColumn;

    private ObservableList<EquipeDTO> equipesList;
    private ObservableList<AmbulanciaDTO> ambulanciasList;
    private ObservableList<ProfissionalDTO> profissionaisList;
    private ObservableList<ProfissionalDTO> profissionaisSelecionados;
    private EquipeDTO equipeSelecionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        ambulanciaIdColumn.setCellValueFactory(new PropertyValueFactory<>("ambulanciaId"));

        equipesList = FXCollections.observableArrayList();
        ambulanciasList = FXCollections.observableArrayList();
        profissionaisList = FXCollections.observableArrayList();
        profissionaisSelecionados = FXCollections.observableArrayList();

        tableView.setItems(equipesList);
        ambulanciaComboBox.setItems(ambulanciasList);
        profissionalComboBox.setItems(profissionaisList);
        profissionaisListView.setItems(profissionaisSelecionados);

        ambulanciaComboBox.setConverter(new StringConverter<AmbulanciaDTO>() {
            @Override
            public String toString(AmbulanciaDTO ambulancia) {
                return ambulancia == null ? "" : ambulancia.getPlaca() + " - " + ambulancia.getBaseNome();
            }

            @Override
            public AmbulanciaDTO fromString(String string) {
                return ambulanciasList.stream()
                        .filter(a -> (a.getPlaca() + " - " + a.getBaseNome()).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        profissionalComboBox.setConverter(new StringConverter<ProfissionalDTO>() {
            @Override
            public String toString(ProfissionalDTO profissional) {
                return profissional == null ? "" : profissional.getNome() + " - " + profissional.getFuncao();
            }

            @Override
            public ProfissionalDTO fromString(String string) {
                return profissionaisList.stream()
                        .filter(p -> (p.getNome() + " - " + p.getFuncao()).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        profissionaisListView.setCellFactory(param -> new ListCell<ProfissionalDTO>() {
            @Override
            protected void updateItem(ProfissionalDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome() + " - " + item.getFuncao());
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            equipeSelecionada = newSelection;
            if (newSelection != null) {
                carregarDadosFormulario(newSelection);
            }
        });

        carregarAmbulancias();
        carregarProfissionais();
        carregarDados();
    }

    @FXML
    private void novo() {
        limparFormulario();
        equipeSelecionada = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void salvar() {
        try {
            if (descricaoField.getText().trim().isEmpty()) {
                mostrarErro("Descrição é obrigatória!");
                return;
            }
            if (ambulanciaComboBox.getSelectionModel().getSelectedItem() == null) {
                mostrarErro("Ambulância é obrigatória!");
                return;
            }
            if (profissionaisSelecionados.isEmpty()) {
                mostrarErro("A equipe deve ter pelo menos um profissional!");
                return;
            }

            EquipeRequestDTO request = new EquipeRequestDTO();
            request.setDescricao(descricaoField.getText().trim());
            request.setAmbulanciaId(ambulanciaComboBox.getSelectionModel().getSelectedItem().getId());
            request.setProfissionalIds(profissionaisSelecionados.stream()
                    .map(ProfissionalDTO::getId)
                    .collect(Collectors.toList()));

            if (equipeSelecionada == null) {
                equipeService.criar(request);
                mostrarSucesso("Equipe criada com sucesso!");
            } else {
                equipeService.atualizar(equipeSelecionada.getId(), request);
                mostrarSucesso("Equipe atualizada com sucesso!");
            }

            limparFormulario();
            carregarDados();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void editar() {
        if (equipeSelecionada == null) {
            mostrarErro("Selecione uma equipe para editar!");
            return;
        }
        carregarDadosFormulario(equipeSelecionada);
    }

    @FXML
    private void excluir() {
        if (equipeSelecionada == null) {
            mostrarErro("Selecione uma equipe para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir a equipe: " + equipeSelecionada.getDescricao() + "?");
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    equipeService.deletar(equipeSelecionada.getId());
                    mostrarSucesso("Equipe excluída com sucesso!");
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
        equipeSelecionada = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void adicionarProfissional() {
        ProfissionalDTO profissional = profissionalComboBox.getSelectionModel().getSelectedItem();
        if (profissional == null) {
            mostrarErro("Selecione um profissional!");
            return;
        }
        if (profissionaisSelecionados.contains(profissional)) {
            mostrarErro("Este profissional já está na equipe!");
            return;
        }
        profissionaisSelecionados.add(profissional);
    }

    @FXML
    private void removerProfissional() {
        ProfissionalDTO profissional = profissionaisListView.getSelectionModel().getSelectedItem();
        if (profissional == null) {
            mostrarErro("Selecione um profissional para remover!");
            return;
        }
        profissionaisSelecionados.remove(profissional);
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) descricaoField.getScene().getWindow();
        stage.close();
    }

    private void carregarDados() {
        try {
            equipesList.clear();
            equipesList.addAll(equipeService.listarTodas());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void carregarAmbulancias() {
        try {
            ambulanciasList.clear();
            ambulanciasList.addAll(ambulanciaService.listarTodas());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar ambulâncias: " + e.getMessage());
        }
    }

    private void carregarProfissionais() {
        try {
            profissionaisList.clear();
            profissionaisList.addAll(profissionalService.listarTodas());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar profissionais: " + e.getMessage());
        }
    }

    private void carregarDadosFormulario(EquipeDTO equipe) {
        descricaoField.setText(equipe.getDescricao());
        AmbulanciaDTO ambulancia = ambulanciasList.stream()
                .filter(a -> a.getId().equals(equipe.getAmbulanciaId()))
                .findFirst()
                .orElse(null);
        ambulanciaComboBox.getSelectionModel().select(ambulancia);

        profissionaisSelecionados.clear();
        if (equipe.getProfissionais() != null) {
            profissionaisSelecionados.addAll(equipe.getProfissionais());
        }
    }

    private void limparFormulario() {
        descricaoField.clear();
        ambulanciaComboBox.getSelectionModel().clearSelection();
        profissionaisSelecionados.clear();
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

