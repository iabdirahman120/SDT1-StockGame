package business.events;

    // record opretter automatisk: felter, konstruktør og getters
    public record StockUpdateEvent(String symbol, double price, String state) {}


