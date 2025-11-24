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
import sosrota.application.dtos.AtendimentoDTO;
import sosrota.application.dtos.AmbulanciaMaisUtilizadaDTO;
import sosrota.application.dtos.OcorrenciaPorGravidadeDTO;
import sosrota.application.services.AtendimentoService;
import sosrota.application.services.OcorrenciaService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class HistoricoController implements Initializable {

    @Autowired
    private AtendimentoService atendimentoService;

    @Autowired
    private OcorrenciaService ocorrenciaService;

    @FXML
    private TableView<AtendimentoDTO> atendimentosTableView;

    @FXML
    private TableColumn<AtendimentoDTO, Long> idColumn;

    @FXML
    private TableColumn<AtendimentoDTO, Long> ocorrenciaIdColumn;

    @FXML
    private TableColumn<AtendimentoDTO, Long> ambulanciaIdColumn;

    @FXML
    private TableColumn<AtendimentoDTO, String> dataHoraDespachoColumn;

    @FXML
    private TableColumn<AtendimentoDTO, String> dataHoraChegadaColumn;

    @FXML
    private TableColumn<AtendimentoDTO, Double> distanciaColumn;

    @FXML
    private DatePicker dataInicioPicker;

    @FXML
    private DatePicker dataFimPicker;

    @FXML
    private DatePicker dataInicioGravidadePicker;

    @FXML
    private DatePicker dataFimGravidadePicker;

    @FXML
    private TableView<AmbulanciaMaisUtilizadaDTO> ambulanciasMaisUtilizadasTableView;

    @FXML
    private TableColumn<AmbulanciaMaisUtilizadaDTO, Integer> posicaoColumn;

    @FXML
    private TableColumn<AmbulanciaMaisUtilizadaDTO, Long> ambulanciaIdColumn2;

    @FXML
    private TableColumn<AmbulanciaMaisUtilizadaDTO, String> placaColumn;

    @FXML
    private TableColumn<AmbulanciaMaisUtilizadaDTO, Long> totalAtendimentosColumn;

    @FXML
    private TableView<OcorrenciaPorGravidadeDTO> ocorrenciasPorGravidadeTableView;

    @FXML
    private TableColumn<OcorrenciaPorGravidadeDTO, String> gravidadeColumn;

    @FXML
    private TableColumn<OcorrenciaPorGravidadeDTO, Long> totalOcorrenciasColumn;

    private ObservableList<AtendimentoDTO> atendimentosList;
    private ObservableList<AmbulanciaMaisUtilizadaDTO> ambulanciasMaisUtilizadasList;
    private ObservableList<OcorrenciaPorGravidadeDTO> ocorrenciasPorGravidadeList;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar colunas de atendimentos
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ocorrenciaIdColumn.setCellValueFactory(new PropertyValueFactory<>("ocorrenciaId"));
        ambulanciaIdColumn.setCellValueFactory(new PropertyValueFactory<>("ambulanciaId"));
        dataHoraDespachoColumn.setCellValueFactory(new PropertyValueFactory<>("dataHoraDespacho"));
        dataHoraChegadaColumn.setCellValueFactory(new PropertyValueFactory<>("dataHoraChegada"));
        distanciaColumn.setCellValueFactory(new PropertyValueFactory<>("distanciaKm"));

        // Configurar colunas de ambulâncias mais utilizadas
        posicaoColumn.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        ambulanciaIdColumn2.setCellValueFactory(new PropertyValueFactory<>("ambulanciaId"));
        placaColumn.setCellValueFactory(new PropertyValueFactory<>("placa"));
        totalAtendimentosColumn.setCellValueFactory(new PropertyValueFactory<>("totalAtendimentos"));

        // Configurar colunas de ocorrências por gravidade
        gravidadeColumn.setCellValueFactory(new PropertyValueFactory<>("gravidade"));
        totalOcorrenciasColumn.setCellValueFactory(new PropertyValueFactory<>("totalOcorrencias"));

        atendimentosList = FXCollections.observableArrayList();
        ambulanciasMaisUtilizadasList = FXCollections.observableArrayList();
        ocorrenciasPorGravidadeList = FXCollections.observableArrayList();

        atendimentosTableView.setItems(atendimentosList);
        ambulanciasMaisUtilizadasTableView.setItems(ambulanciasMaisUtilizadasList);
        ocorrenciasPorGravidadeTableView.setItems(ocorrenciasPorGravidadeList);

        carregarTodosAtendimentos();
    }

    @FXML
    private void filtrarAtendimentos() {
        LocalDate inicio = dataInicioPicker.getValue();
        LocalDate fim = dataFimPicker.getValue();

        if (inicio == null || fim == null) {
            mostrarErro("Selecione ambas as datas (início e fim)!");
            return;
        }

        try {
            String dataInicioStr = inicio.format(DATE_FORMATTER);
            String dataFimStr = fim.format(DATE_FORMATTER);
            atendimentosList.clear();
            atendimentosList.addAll(atendimentoService.listarPorPeriodo(dataInicioStr, dataFimStr));
        } catch (Exception e) {
            mostrarErro("Erro ao filtrar atendimentos: " + e.getMessage());
        }
    }

    @FXML
    private void limparFiltro() {
        dataInicioPicker.setValue(null);
        dataFimPicker.setValue(null);
        carregarTodosAtendimentos();
    }

    @FXML
    private void consultarAmbulanciasMaisUtilizadas() {
        try {
            ambulanciasMaisUtilizadasList.clear();
            List<AmbulanciaMaisUtilizadaDTO> resultados = atendimentoService.consultarAmbulanciasMaisUtilizadas();
            ambulanciasMaisUtilizadasList.addAll(resultados);
            
            if (resultados.isEmpty()) {
                mostrarMensagem("Consulta SQL", "Nenhuma ambulância encontrada com atendimentos registrados.");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao consultar ambulâncias mais utilizadas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void consultarOcorrenciasPorGravidade() {
        LocalDate inicio = dataInicioGravidadePicker.getValue();
        LocalDate fim = dataFimGravidadePicker.getValue();

        if (inicio == null || fim == null) {
            mostrarErro("Selecione ambas as datas (início e fim) para a consulta!");
            return;
        }

        if (inicio.isAfter(fim)) {
            mostrarErro("A data de início deve ser anterior à data de fim!");
            return;
        }

        try {
            LocalDateTime dataInicio = inicio.atStartOfDay();
            LocalDateTime dataFim = fim.atTime(23, 59, 59);
            
            ocorrenciasPorGravidadeList.clear();
            List<OcorrenciaPorGravidadeDTO> resultados = 
                    ocorrenciaService.consultarOcorrenciasPorGravidadeEPeriodo(dataInicio, dataFim);
            ocorrenciasPorGravidadeList.addAll(resultados);
            
            if (resultados.isEmpty()) {
                mostrarMensagem("Consulta SQL", "Nenhuma ocorrência encontrada no período selecionado.");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao consultar ocorrências por gravidade: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) atendimentosTableView.getScene().getWindow();
        stage.close();
    }

    private void carregarTodosAtendimentos() {
        try {
            atendimentosList.clear();
            atendimentosList.addAll(atendimentoService.listarTodos());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar atendimentos: " + e.getMessage());
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

