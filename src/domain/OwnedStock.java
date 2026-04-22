package domain;

public class OwnedStock {
    private int id;
    private int portfolioId;
    private String stockSymbol;
    private int numberOfShares;

    public OwnedStock(int id, int portfolioId, String stockSymbol, int numberOfShares) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.stockSymbol= stockSymbol;
        this.numberOfShares= numberOfShares;
    }

    public int getId() {
        return id;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares){
        this.numberOfShares = numberOfShares;
    }
}
