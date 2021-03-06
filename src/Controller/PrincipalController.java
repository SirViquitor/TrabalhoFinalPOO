package Controller;

import Model.Consumo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrincipalController extends GeneralController{
    @FXML private Label custoFesta, lucroFesta;

    @FXML
    void initialize(){
        MainController.setListener((newScreen, userData) -> {
            if (newScreen.equals("Principal")) {
                iniciaDados();
            }
        });
    }

    void iniciaDados() {
        try {
            ResultSet analytics = Consumo.analytics();
            custoFesta.setText("Custo: R$ " + analytics.getString("custo"));
            lucroFesta.setText("Lucro: R$ " + analytics.getString("lucro"));
        } catch (SQLException e){
            alerta("Woops");
        }

    }
    @FXML void gerenciarClientes(){
        MainController.changeScreen("MenuClientes");
    }
    @FXML void gerenciarProdutos(){
        MainController.changeScreen("MenuProdutos");
    }

    @FXML void adicionarCredito() {
        openModal("AddCredito.fxml");
    }
    @FXML void consultarConsumo(){ MainController.changeScreen("MenuConsumo"); }
    @FXML void novoConsumo(){ openModal("NovoConsumo.fxml"); }
    @FXML void pagarConsumo(){ openModal("PagarConsumo.fxml"); }
}
