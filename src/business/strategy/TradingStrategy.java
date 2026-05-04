package business.strategy;

public interface TradingStrategy {
    // Beregner hvor mange aktier der skal købes baseret på balance og pris
    int calculateQuantity(double balance, double price);
}
