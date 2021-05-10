package Controller;

import Model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProdutoController extends GeneralController{
    @FXML private Label idProduto;
    @FXML private TextField descricao, quantidade, precoCusto, precoVenda;
    @FXML private TableView<ResultSet> tableProduto;
    @FXML private TableColumn<Produto, Integer> table_id;
    @FXML private TableColumn<Produto, String> table_titulo;
    @FXML private TableColumn<Produto, Integer> table_quantidade;
    @FXML private TableColumn<Produto, Float> table_custo;
    @FXML private TableColumn<Produto, Float> table_preco;
    @FXML private ObservableList<ResultSet> listProdutos = FXCollections.observableArrayList();

    @FXML
    void initialize() throws SQLException {
        configuraColunas();
        MainController.setListener((newScreen, userData) -> {
            if (newScreen.equals("MenuProdutos")) mostraTabela();
        });
        if(modalScreen){
            if(idProduto == null) idProduto = new Label();
            idProduto.setText(Integer.toString(Produto.nextId()));
            modalScreen = false;
        }
    }

    void mostraTabela(){
        try {
            ResultSet resultSet = Produto.read();
            while (resultSet.next()) {
                listProdutos.add(resultSet);
                atualizarTabela();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML void pesquisar() {}

    @FXML
    void cadastrar(){
        openModal("AddProduto.fxml");
    }

    @FXML void confirmar() {
        try {
            Produto.create(
                descricao.getText(),
                Integer.parseInt(quantidade.getText()),
                Float.parseFloat(precoCusto.getText()),
                Float.parseFloat(precoVenda.getText())
            );
            cancelar();
            atualizarTabela();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            modal.close();
        }
    }

    void configuraColunas(){
        table_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        table_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        table_quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        table_custo.setCellValueFactory(new PropertyValueFactory<>("valor_custo"));
        table_preco.setCellValueFactory(new PropertyValueFactory<>("valor_venda"));
    }

    void atualizarTabela(){
        tableProduto.getItems().clear();
        tableProduto.getItems().setAll(listProdutos);
    }

}
