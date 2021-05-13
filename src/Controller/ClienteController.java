package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteController extends GeneralController {
    @FXML private TextField RG, RGHome, nomeCliente, valorCredito, tipoEntrada;
    @FXML private ChoiceBox<String> tipoCliente;
    @FXML private ComboBox<String> tipoClienteFiltro;
    @FXML private ListView<String> listClientes;
    private static int idUpdate;

    @FXML
    void initialize(){
        if(tipoCliente == null) tipoCliente = new ChoiceBox<>();
        if(tipoClienteFiltro == null) tipoClienteFiltro = new ComboBox<>();
        MainController.setListener((newScreen, userData) -> {
            if (newScreen.equals("MenuClientes")){
                mostraTabela();
            }
        });
        if(modalScreen){
            setReadCliente();
            idUpdate = 0;
            modalScreen = false;
        }
    }
    void setReadCliente(){
        try {
            ResultSet dados = Cliente.read(Integer.toString(idUpdate));
            RG.setText(dados.getString("rg"));
            nomeCliente.setText(dados.getString("nome"));
            tipoEntrada.setText(dados.getString("tipo_entrada"));
        } catch (SQLException e) {
            alerta(e.getMessage());
        }
    }
    void mostraTabela(){
        if(listClientes == null) listClientes = new ListView<>();
        listClientes.getItems().clear();
        try {
            ResultSet clientes = Cliente.read();
            while (clientes.next()){
                listClientes.getItems().add(
                    clientes.getString("rg") + " - " +
                    clientes.getString("nome") + " - (" +
                    clientes.getString("tipo_entrada") + ")"
                );
            }
        } catch (SQLException e) {
            alerta(e.getMessage());
        }

    }

    void mostraTabela(ResultSet dados){
        if(listClientes == null) listClientes = new ListView<>();
        listClientes.getItems().clear();
        try {
            while (dados.next()){
                listClientes.getItems().add(
                    dados.getString("rg") + " - " +
                    dados.getString("nome") + " - (" +
                    dados.getString("tipo_entrada") + ")"
                );
            }
        } catch (SQLException e) {
            alerta(e.getMessage());
        }
    }

    @FXML void filtraCategoria(){
        try {
            ResultSet clientes;
            switch (tipoClienteFiltro.getSelectionModel().getSelectedItem()){
                case "Pista": clientes = Pista.read(); break;
                case "Camarote": clientes = Camarote.read(); break;
                case "VIP": clientes = Vip.read(); break;
                default: clientes = Cliente.read(); break;
            }
            listClientes.getItems().clear();
            while (clientes.next()){
                listClientes.getItems().add(
                    clientes.getString("rg") + " - " +
                    clientes.getString("nome") + " - (" +
                    clientes.getString("tipo_entrada") + ")"
                );
            }
        } catch (SQLException e) {
            alerta(e.getMessage());
        }
    }

    @FXML void cadastrar() {
        openModal("AddCliente.fxml");
    }

    @FXML void confirmarCadastro() {
        try {
            if(tipoCliente == null) tipoCliente = new ChoiceBox<>();
            String rg = RG.getText();
            String nome = nomeCliente.getText();
            switch (tipoCliente.getSelectionModel().getSelectedItem()){
                case "VIP": Vip.create(rg, nome); break;
                case "Camarote": Camarote.create(rg, nome, 0F); break;
                case "Pista": Pista.create(rg, nome, 0F); break;
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Preencha o nome do cliente!");
        }
        mostraTabela();
        cancelar();
    }
    @FXML void confirmarAddCredito() {
        try {
            CamarotePista.adicionarCredito(
                Integer.parseInt(RG.getText()),
                Float.parseFloat(valorCredito.getText())
            );
            cancelar();
        } catch (Exception e) {
            alerta(e.getMessage());
        }
    }

    void maskRG(){
        RG.setText(RG.getText().replaceAll("[^0-9]", ""));
        RG.positionCaret(RG.getLength());
    }
    @FXML void filtraRG(){
        maskRG();
    }
    @FXML void buscaRG(){
        try {
            maskRG();
            nomeCliente.setText(
                CamarotePista.read(
                    RG.getText()
                ).getString("nome")
            );

        } catch (SQLException | IllegalArgumentException e) {
            nomeCliente.setText("RG inválido para esta operação");
        } catch (Exception e) {
            alerta(e.getMessage());
        }
    }
    @FXML void buscaRGHome(){
        try {
            RGHome.setText(RGHome.getText().replaceAll("[^0-9]", ""));
            RGHome.positionCaret(RGHome.getLength());
            if (RGHome.getText().isEmpty()){
                mostraTabela();
            }else {
                mostraTabela(
                    Cliente.readWithLike(RGHome.getText())
                );
            }
        } catch (SQLException | IllegalArgumentException e) {
            alerta(e.getMessage());
        }
    }
    @FXML void maskPreco(){
        valorCredito.setText(valorCredito.getText().replaceAll(",", "."));
        valorCredito.setText(valorCredito.getText().replaceAll("[^0-9.]", ""));
        valorCredito.positionCaret(valorCredito.getLength());
    }

    @FXML void updatemodal(){
        String[] explode = listClientes.getSelectionModel().getSelectedItem().split(" - ");
        idUpdate = Integer.parseInt(explode[0]);
        openModal("ReadCliente.fxml");
    }
}
