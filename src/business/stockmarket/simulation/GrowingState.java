package business.stockmarket.simulation;

public class GrowingState implements StockState {
    @Override
    public double calculatePriceChange(){
        return Math.random() * 10; //altid positiv
    }

    @Override
    public String getName(){
        return "Growing";
    }
}
