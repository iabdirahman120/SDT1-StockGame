package business.stockmarket.simulation;

//nulstiller aktien og sender den tilbage til Steady
public class ResetState implements StockState{

    @Override
    public double calculatePriceChange() {
        return 50;  // sætter prisen op igen
    }

    @Override
    public String getName() {
        return "Reset";
    }
}
