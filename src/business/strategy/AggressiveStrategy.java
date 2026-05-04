package business.strategy;

public class AggressiveStrategy implements TradingStrategy {

    // Aggressiv — brug 50% af balance
    @Override
    public int calculateQuantity(double balance, double price) {
        return (int) ((balance * 0.5) / price);
    }
}
