package business.adapter;

import domain.Stock;
import persistence.StockDao;

import java.util.ArrayList;
import java.util.List;

// Adapter — oversætter ExternalStockService til vores StockDao interface
public class StockServiceAdapter implements StockDao {

    // Den eksterne service vi vil tilpasse
    private final ExternalStockService externalService;

    public StockServiceAdapter(ExternalStockService externalService) {
        this.externalService = externalService;
    }

    // Oversætter fetchStock() → getById()
    @Override
    public Stock getById(String symbol) {
        String[] data = externalService.fetchStock(symbol);
        if (data == null) return null;
        return new Stock(data[0], data[1], Double.parseDouble(data[2]), data[3]);
    }

    // Oversætter fetchAllStocks() → getAll()
    @Override
    public List<Stock> getAll() {
        String[][] allData = externalService.fetchAllStocks();
        List<Stock> stocks = new ArrayList<>();
        for (String[] data : allData) {
            stocks.add(new Stock(data[0], data[1], Double.parseDouble(data[2]), data[3]));
        }
        return stocks;
    }

    // Disse metoder understøttes ikke af det eksterne system
    @Override public void create(Stock s) {}
    @Override public void update(Stock s) {}
    @Override public void delete(String id) {}
}
