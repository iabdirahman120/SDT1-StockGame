package business.stockmarket.simulation;

public class DecliningState implements StockState {
    @Override
    public double calculatePriceChange(){
        return -(Math.random()*10); //altid negativt
    }

    @Override
    public String getName() {
        return "Declining";
    }

}
