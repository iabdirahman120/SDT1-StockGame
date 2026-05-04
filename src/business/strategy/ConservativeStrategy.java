package business.strategy;

public class ConservativeStrategy implements TradingStrategy {

    // Konservativ — brug kun 10% af balance
    @Override
    public int calculateQuantity(double balance, double price) {
        return (int) ((balance * 0.1) / price);
    }
}
