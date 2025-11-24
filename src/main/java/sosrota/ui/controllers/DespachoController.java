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
import sosrota.application.dtos.OcorrenciaDTO;
import sosrota.application.dtos.AmbulanciaSugeridaDTO;
import sosrota.application.dtos.DespachoSugestaoDTO;
import sosrota.application.services.OcorrenciaService;
import sosrota.application.services.DespachoService;
import sosrota.domain.models.StatusOcorrencia;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class DespachoController implements Initializable {

    @Autowired
    private OcorrenciaService ocorrenciaService;

    @Autowired
    private DespachoService despachoService;

    @FXML
    private TableView<OcorrenciaDTO> ocorrenciasTableView;

    @FXML
    private TableColumn<OcorrenciaDTO, Long> ocorrenciaIdColumn;

    @FXML
    private TableColumn<OcorrenciaDTO, String> ocorrenciaTipoColumn;

    @FXML
    private TableColumn<OcorrenciaDTO, String> ocorrenciaGravidadeColumn;

    @FXML
    private TableColumn<OcorrenciaDTO, String> ocorrenciaBairroColumn;

    @FXML
    private TableView<AmbulanciaSugeridaDTO> ambulanciasTableView;

    @FXML
    private TableColumn<AmbulanciaSugeridaDTO, String> placaColumn;

    @FXML
    private TableColumn<AmbulanciaSugeridaDTO, String> tipoColumn;

    @FXML
    private TableColumn<AmbulanciaSugeridaDTO, String> baseColumn;

    @FXML
    private TableColumn<AmbulanciaSugeridaDTO, Double> distanciaColumn;

    @FXML
    private TableColumn<AmbulanciaSugeridaDTO, Integer> tempoColumn;

    @FXML
    private TableColumn<AmbulanciaSugeridaDTO, Boolean> slaColumn;

    @FXML
    private Label infoLabel;

    private ObservableList<OcorrenciaDTO> ocorrenciasList;
    private ObservableList<AmbulanciaSugeridaDTO> ambulanciasSugeridasList;
    private OcorrenciaDTO ocorrenciaSelecionada;
    private AmbulanciaSugeridaDTO ambulanciaSelecionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ocorrenciaIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ocorrenciaTipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        ocorrenciaGravidadeColumn.setCellValueFactory(new PropertyValueFactory<>("gravidade"));
        ocorrenciaBairroColumn.setCellValueFactory(new PropertyValueFactory<>("bairroNome"));

        placaColumn.setCellValueFactory(new PropertyValueFactory<>("placa"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        baseColumn.setCellValueFactory(new PropertyValueFactory<>("baseNome"));
        distanciaColumn.setCellValueFactory(new PropertyValueFactory<>("distanciaKm"));
        tempoColumn.setCellValueFactory(new PropertyValueFactory<>("tempoEstimadoMinutos"));
        slaColumn.setCellValueFactory(new PropertyValueFactory<>("slaCumprido"));

        ocorrenciasList = FXCollections.observableArrayList();
        ambulanciasSugeridasList = FXCollections.observableArrayList();
        ocorrenciasTableView.setItems(ocorrenciasList);
        ambulanciasTableView.setItems(ambulanciasSugeridasList);

        ocorrenciasTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            ocorrenciaSelecionada = newSelection;
            ambulanciasSugeridasList.clear();
            if (newSelection != null) {
                infoLabel.setText("Ocorrência selecionada: ID " + newSelection.getId() + " - " + newSelection.getTipo());
            } else {
                infoLabel.setText("Selecione uma ocorrência e clique em 'Buscar Sugestões'");
            }
        });

        ambulanciasTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            ambulanciaSelecionada = newSelection;
        });

        carregarOcorrenciasAbertas();
    }

    @FXML
    private void buscarSugestoes() {
        if (ocorrenciaSelecionada == null) {
            mostrarErro("Selecione uma ocorrência primeiro!");
            return;
        }

        try {
            DespachoSugestaoDTO sugestao = despachoService.sugerirAmbulancias(ocorrenciaSelecionada.getId());
            
            ambulanciasSugeridasList.clear();
            if (sugestao.getAmbulanciasSugeridas() != null && !sugestao.getAmbulanciasSugeridas().isEmpty()) {
                ambulanciasSugeridasList.addAll(sugestao.getAmbulanciasSugeridas());
                infoLabel.setText(String.format("Encontradas %d ambulância(s) sugerida(s). Distância: %.2f km, Tempo: %d min",
                        sugestao.getAmbulanciasSugeridas().size(),
                        sugestao.getDistanciaKm(),
                        sugestao.getTempoEstimadoMinutos()));
            } else {
                infoLabel.setText("Nenhuma ambulância disponível que atenda aos critérios (SLA, equipe completa, tipo adequado).");
                mostrarErro("Nenhuma ambulância disponível que atenda aos critérios de despacho.");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao buscar sugestões: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void executarDespacho() {
        if (ocorrenciaSelecionada == null) {
            mostrarErro("Selecione uma ocorrência primeiro!");
            return;
        }

        if (ambulanciaSelecionada == null) {
            mostrarErro("Selecione uma ambulância sugerida para despachar!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Despacho");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText(String.format(
                "Deseja realmente despachar a ambulância %s para a ocorrência ID %d?\n\n" +
                "Distância: %.2f km\n" +
                "Tempo estimado: %d minutos",
                ambulanciaSelecionada.getPlaca(),
                ocorrenciaSelecionada.getId(),
                ambulanciaSelecionada.getDistanciaKm(),
                ambulanciaSelecionada.getTempoEstimadoMinutos()
        ));
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    despachoService.executarDespacho(ocorrenciaSelecionada.getId(), ambulanciaSelecionada.getAmbulanciaId());
                    mostrarSucesso("Despacho executado com sucesso! A ambulância " + ambulanciaSelecionada.getPlaca() + 
                            " foi designada para a ocorrência ID " + ocorrenciaSelecionada.getId());
                    carregarOcorrenciasAbertas();
                    ambulanciasSugeridasList.clear();
                    ocorrenciaSelecionada = null;
                    ambulanciaSelecionada = null;
                    infoLabel.setText("Despacho executado com sucesso!");
                } catch (Exception e) {
                    mostrarErro("Erro ao executar despacho: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) ocorrenciasTableView.getScene().getWindow();
        stage.close();
    }

    private void carregarOcorrenciasAbertas() {
        try {
            ocorrenciasList.clear();
            ocorrenciasList.addAll(ocorrenciaService.listarPorStatus(StatusOcorrencia.ABERTA.name()));
        } catch (Exception e) {
            mostrarErro("Erro ao carregar ocorrências: " + e.getMessage());
        }
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

