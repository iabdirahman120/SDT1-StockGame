package business.stockmarket.simulation;

import java.util.ArrayList;
import java.util.List;

public class StockMarket {
    private static StockMarket instance; // den ene instans af StockMarket
    private List<LiveStock> stocks = new ArrayList<>(); // liste over alle aktive aktier

    private StockMarket() {} // privat konstruktør - ingen kan oprette ny instans udefra

    // returnerer altid samme instans - Singleton
    public static StockMarket getInstance() {
        if (instance == null) {
            instance = new StockMarket();
        }
        return instance;
    }

    // hent alle aktier
    public List<LiveStock> getStocks() {
        return stocks;
    }
}
