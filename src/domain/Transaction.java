package domain;

public class Transaction {

    private int id;
    private int portfolioId;
    private String stockSymbol;
    private String type;
    private int quantity;
    private double pricePerShare;
    private double totalAmount;
    private double fee;
    private String timeStamp;

    public Transaction (int id, int portfolioId, String stockSymbol, String type, int quantity, double pricePerShare,
                        double totalAmount, double fee, String timeStamp){
        this.id = id;
        this.portfolioId= portfolioId;
        this.stockSymbol= stockSymbol;
        this.type=type;
        this.quantity=quantity;
        this.pricePerShare=pricePerShare;
        this.totalAmount=totalAmount;
        this.fee=fee;
        this.timeStamp=timeStamp;
    }

    public int getId() {
        return id;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public String getStockSymbol(){
        return stockSymbol;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getFee() {
        return fee;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
