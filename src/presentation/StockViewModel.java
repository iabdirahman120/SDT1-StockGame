package presentation;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class StockViewModel {

    // Observable properties — View opdaterer sig automatisk når disse ændres
    private final SimpleStringProperty symbol = new SimpleStringProperty();
    private final SimpleStringProperty name   = new SimpleStringProperty();
    private final SimpleDoubleProperty price  = new SimpleDoubleProperty();
    private final SimpleStringProperty state  = new SimpleStringProperty();

    public StockViewModel(String symbol, String name, double price, String state) {
        this.symbol.set(symbol);
        this.name.set(name);
        this.price.set(price);
        this.state.set(state);
    }

    // Getters til JavaFX tabel
    public String getSymbol() { return symbol.get(); }
    public String getName()   { return name.get(); }
    public double getPrice()  { return price.get(); }
    public String getState()  { return state.get(); }

    // Property-getters bruges af JavaFX til data binding
    public SimpleStringProperty symbolProperty() { return symbol; }
    public SimpleStringProperty nameProperty()   { return name; }
    public SimpleDoubleProperty priceProperty()  { return price; }
    public SimpleStringProperty stateProperty()  { return state; }

    // Opdaterer pris og state live fra StockMarket
    public void update(double newPrice, String newState) {
        price.set(newPrice);
        state.set(newState);
    }
}

