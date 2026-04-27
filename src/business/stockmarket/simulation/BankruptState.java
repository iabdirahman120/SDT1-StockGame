package business.stockmarket.simulation;

public class BankruptState implements StockState{

    @Override
    public double calculatePriceChange() {
        return 0;
    }

    @Override
    public String getName() {
        return "Bankrupt";
    }

}
