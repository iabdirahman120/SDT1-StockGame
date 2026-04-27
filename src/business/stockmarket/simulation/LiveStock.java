package business.stockmarket.simulation;

public class LiveStock {
    private String symbol;        // aktiens unikke kode fx "AAPL"
    private double currentPrice;  // aktuel pris
    private StockState currentState; // nuværende tilstand

    // opretter ny aktie med startpris og steady state
    public LiveStock(String symbol, double currentPrice) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.currentState = new SteadyState();
    }

    // opdaterer prisen baseret på nuværende state
    public void updatePrice() {
        double change = currentState.calculatePriceChange(); // hent prisændring fra state
        currentPrice += change; // opdater prisen
        if (currentPrice <= 0) { // er aktien gået konkurs?
            currentState = new BankruptState();
        }
    }

    // bruges af states til at skifte tilstand
    public void setState(StockState state) {
        this.currentState = state;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    // returnerer statens navn som String til persistens
    public String getCurrentStateName() {
        return currentState.getName();
    }
}
