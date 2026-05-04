package business.adapter;

// Simulerer et eksternt aktiesystem med anderledes metodenavne
// Vi kan ikke ændre denne klasse — den kommer fra et eksternt bibliotek
public class ExternalStockService {

    // Eksternt system bruger "fetchStock" i stedet for "getById"
    public String[] fetchStock(String ticker) {
        // Returnerer data som array: [symbol, navn, pris, state]
        if (ticker.equals("GOOG")) {
            return new String[]{"GOOG", "Google", "150.0", "Growing"};
        }
        return null;
    }

    // Eksternt system bruger "fetchAllStocks" i stedet for "getAll"
    public String[][] fetchAllStocks() {
        return new String[][]{
                {"GOOG", "Google",    "150.0", "Growing"},
                {"MSFT", "Microsoft", "200.0", "Steady"}
        };
    }
}
