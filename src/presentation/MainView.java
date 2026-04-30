package presentation;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

    private PortfolioViewModel viewModel;

    // Sættes før Application.launch() kaldes
    public static PortfolioViewModel sharedViewModel;

    @Override
    public void start(Stage stage) {
        viewModel = sharedViewModel;

        // --- BALANCE LABEL ---
        Label balanceLabel = new Label();
        // Bind label til balance property → opdateres automatisk
        balanceLabel.textProperty().bind(
                viewModel.balanceProperty().asString("Balance: %.2f kr")
        );

        // --- AKTIE TABEL ---
        TableView<StockViewModel> table = new TableView<>();
        table.setItems(viewModel.getStocks());

        TableColumn<StockViewModel, String> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));

        TableColumn<StockViewModel, String> nameCol = new TableColumn<>("Navn");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StockViewModel, Double> priceCol = new TableColumn<>("Pris");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<StockViewModel, String> stateCol = new TableColumn<>("State");
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));

        table.getColumns().addAll(symbolCol, nameCol, priceCol, stateCol);

        // --- KØB / SÆLG KNAPPER ---
        TextField symbolField   = new TextField();
        symbolField.setPromptText("Symbol (fx AAPL)");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Antal");

        Button buyButton  = new Button("Køb");
        Button sellButton = new Button("Sælg");

        // Køb knap — henter symbol og antal fra tekstfelter
        buyButton.setOnAction(e -> {
            String symbol = symbolField.getText().toUpperCase();
            int quantity  = Integer.parseInt(quantityField.getText());
            viewModel.buyStock(symbol, quantity);
        });

        // Sælg knap
        sellButton.setOnAction(e -> {
            String symbol = symbolField.getText().toUpperCase();
            int quantity  = Integer.parseInt(quantityField.getText());
            viewModel.sellStock(symbol, quantity);
        });

        HBox inputBox = new HBox(10, symbolField, quantityField, buyButton, sellButton);

        // --- LAYOUT ---
        VBox root = new VBox(10, balanceLabel, table, inputBox);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 700, 500));
        stage.setTitle("Stock Game");
        stage.show();
    }
}
