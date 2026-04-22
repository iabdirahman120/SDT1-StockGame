package domain;

public class StockPriceHistory {

    private int id;
    private String stockSymbol;
    private double price;
    private String timestamp;

    public StockPriceHistory(int id, String stockSymbol, double price, String timestamp){
        this.id=id;
        this.stockSymbol=stockSymbol;
        this.price=price;
        this.timestamp=timestamp;
    }

    public int getId() {
        return id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public double getPrice() {
        return price;
    }

    public String getTimestamp() {
        return timestamp;
    }
}


