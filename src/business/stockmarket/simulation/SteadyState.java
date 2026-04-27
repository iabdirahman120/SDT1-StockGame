package business.stockmarket.simulation;

public class SteadyState implements StockState{

    @Override
    public double calculatePriceChange() {
        // lille ændring op eller ned
        return (Math.random() * 2 - 1) * 2;
    }

    @Override
    public String getName() {
        return "Steady";
    }
}
