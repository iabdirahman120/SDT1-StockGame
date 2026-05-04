import business.adapter.ExternalStockService;
import business.adapter.StockServiceAdapter;
import domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockServiceAdapterTest {

    private StockServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        // Opret adapter med det eksterne system
        adapter = new StockServiceAdapter(new ExternalStockService());
    }

    @Test
    void getById_returnererKorrektAktie() {
        // Act
        Stock stock = adapter.getById("GOOG");

        // Assert — adapter oversætter korrekt fra eksternt format
        assertNotNull(stock);
        assertEquals("GOOG",    stock.getSymbol());
        assertEquals("Google",  stock.getName());
        assertEquals(150.0,     stock.getCurrentPrice(), 0.01);
        assertEquals("Growing", stock.getCurrentState());
    }

    @Test
    void getById_ukendt_returnererNull() {
        // Act
        Stock stock = adapter.getById("UKENDT");

        // Assert
        assertNull(stock);
    }

    @Test
    void getAll_returnererAllAktier() {
        // Act
        List<Stock> stocks = adapter.getAll();

        // Assert — to aktier fra det eksterne system
        assertEquals(2, stocks.size());
        assertEquals("GOOG", stocks.get(0).getSymbol());
        assertEquals("MSFT", stocks.get(1).getSymbol());
    }
}
