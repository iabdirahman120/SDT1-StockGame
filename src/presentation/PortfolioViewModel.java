package presentation;

import business.TradingService;
import business.events.StockUpdateEvent;
import business.stockmarket.simulation.StockListener;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PortfolioViewModel implements StockListener {

    // Observable properties — View binder sig til disse
    private final SimpleDoubleProperty balance = new SimpleDoubleProperty();
    private final ObservableList<StockViewModel> stocks = FXCollections.observableArrayList();

    private final TradingService tradingService;
    private final int portfolioId;

    public PortfolioViewModel(TradingService tradingService, int portfolioId, double startBalance) {
        this.tradingService = tradingService;
        this.portfolioId = portfolioId;
        this.balance.set(startBalance);
    }

    // Kaldes af StockMarket når en aktie opdaterer sin pris
    @Override
    public void onStockUpdated(StockUpdateEvent event) {
        // Platform.runLater sikrer at UI opdateres på JavaFX-tråden
        Platform.runLater(() -> {
            for (StockViewModel svm : stocks) {
                if (svm.getSymbol().equals(event.symbol())) {
                    svm.update(event.price(), event.state());
                }
            }
        });
    }

    // Tilføjer en aktie til listen (bruges ved opstart)
    public void addStock(StockViewModel stock) {
        stocks.add(stock);
    }

    // Køb aktie og opdater balance
    public void buyStock(String symbol, int quantity) {
        tradingService.buyStock(portfolioId, symbol, quantity);
    }

    // Sælg aktie og opdater balance
    public void sellStock(String symbol, int quantity) {
        tradingService.sellStock(portfolioId, symbol, quantity);
    }

    // Opdater balance manuelt efter køb/salg
    public void setBalance(double newBalance) {
        Platform.runLater(() -> balance.set(newBalance));
    }

    // Property-getters til View binding
    public SimpleDoubleProperty balanceProperty()        { return balance; }
    public ObservableList<StockViewModel> getStocks()    { return stocks; }
    public double getBalance()                           { return balance.get(); }
}
