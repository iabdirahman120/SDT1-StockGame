package business.strategy;

public class BalancedStrategy implements TradingStrategy {

    // Balanceret — brug 25% af balance
    @Override
    public int calculateQuantity(double balance, double price) {
        return (int) ((balance * 0.25) / price);
    }
}
