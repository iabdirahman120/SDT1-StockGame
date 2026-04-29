package business;

import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.OwnedStockDao;
import persistence.PortfolioDao;
import persistence.StockDao;
import persistence.UnitOfWork;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradingServiceTest {

    // --- FAKE IMPLEMENTATIONER (mocks) ---

    // Fake UnitOfWork — gør ingenting, bare husker om commit/rollback blev kaldt
    static class FakeUow implements UnitOfWork {
        boolean committed = false;
        boolean rolledBack = false;

        @Override public void beginTransaction() {}
        @Override public void commit()    { committed = true; }
        @Override public void rollback()  { rolledBack = true; }
    }

    // Fake StockDao — returnerer en aktie vi selv bestemmer
    static class FakeStockDao implements StockDao {
        Stock stock; // den aktie vi vil returnere

        @Override public Stock getById(String id) { return stock; }
        @Override public List<Stock> getAll()     { return new ArrayList<>(); }
        @Override public void create(Stock s)     {}
        @Override public void update(Stock s)     {}
        @Override public void delete(String id)   {}
    }

    // Fake PortfolioDao — returnerer en portfolio vi selv bestemmer
    static class FakePortfolioDao implements PortfolioDao {
        Portfolio portfolio;

        @Override public Portfolio getById(int id)    { return portfolio; }
        @Override public List<Portfolio> getAll()     { return new ArrayList<>(); }
        @Override public void create(Portfolio p)     {}
        @Override public void update(Portfolio p)     { portfolio = p; } // gem ændringen
        @Override public void delete(int id)          {}
    }

    // Fake OwnedStockDao — husker hvilke aktier der er købt
    static class FakeOwnedStockDao implements OwnedStockDao {
        List<OwnedStock> stocks = new ArrayList<>();

        @Override public OwnedStock getById(int id)    { return null; }
        @Override public List<OwnedStock> getAll()     { return stocks; }
        @Override public void create(OwnedStock os)    { stocks.add(os); }
        @Override public void update(OwnedStock os)    {}
        @Override public void delete(int id)           { stocks.removeIf(s -> s.getId() == id); }
    }

    // --- TEST SETUP ---
    FakeUow uow;
    FakeStockDao stockDao;
    FakePortfolioDao portfolioDao;
    FakeOwnedStockDao ownedStockDao;
    TradingService service;

    @BeforeEach
    void setUp() {
        // Opretter friske fakes før hver test
        uow           = new FakeUow();
        stockDao      = new FakeStockDao();
        portfolioDao  = new FakePortfolioDao();
        ownedStockDao = new FakeOwnedStockDao();
        service       = new TradingService(uow, stockDao, portfolioDao, ownedStockDao);
    }

    // --- TESTS ---

    @Test
    void buyStock_trekkerPengeFraBalance() {
        // Arrange — sæt aktie og portfolio op
        stockDao.stock          = new Stock("AAPL", "Apple", 100.0, "Steady");
        portfolioDao.portfolio  = new Portfolio(1, 10000.0);

        // Act — køb 10 aktier
        service.buyStock(1, "AAPL", 10);

        // Assert — balance skal være reduceret (100 * 10 + 2% fee = 1020)
        assertEquals(8980.0, portfolioDao.portfolio.getCurrentBalance(), 0.01);
        assertTrue(uow.committed);
    }

    @Test
    void buyStock_ikkeNokPenge_rollback() {
        // Arrange — balance kun 500, aktie koster 100 * 10 = 1020 med fee
        stockDao.stock         = new Stock("AAPL", "Apple", 100.0, "Steady");
        portfolioDao.portfolio = new Portfolio(1, 500.0);

        // Act
        service.buyStock(1, "AAPL", 10);

        // Assert — rollback skal være sket, balance uændret
        assertTrue(uow.rolledBack);
        assertEquals(500.0, portfolioDao.portfolio.getCurrentBalance(), 0.01);
    }

    @Test
    void buyStock_negativtAntal_rollback() {
        // Arrange
        stockDao.stock         = new Stock("AAPL", "Apple", 100.0, "Steady");
        portfolioDao.portfolio = new Portfolio(1, 10000.0);

        // Act — prøv at købe -5 aktier
        service.buyStock(1, "AAPL", -5);

        // Assert — ugyldigt input → rollback
        assertTrue(uow.rolledBack);
    }

    @Test
    void buyStock_aktieErKonkurs_rollback() {
        // Arrange — aktie med state "Bankrupt"
        stockDao.stock         = new Stock("AAPL", "Apple", 0.0, "Bankrupt");
        portfolioDao.portfolio = new Portfolio(1, 10000.0);

        // Act
        service.buyStock(1, "AAPL", 5);

        // Assert — konkurs aktie → rollback
        assertTrue(uow.rolledBack);
    }

    @Test
    void sellStock_tilfoejrPengeTilBalance() {
        // Arrange — ejer 20 aktier, sælger 10
        stockDao.stock         = new Stock("AAPL", "Apple", 100.0, "Steady");
        portfolioDao.portfolio = new Portfolio(1, 5000.0);
        ownedStockDao.stocks.add(new OwnedStock(1, 1, "AAPL", 20));

        // Act
        service.sellStock(1, "AAPL", 10);

        // Assert — 100 * 10 - 2% fee = 980 tilføjet
        assertEquals(5980.0, portfolioDao.portfolio.getCurrentBalance(), 0.01);
        assertTrue(uow.committed);
    }

    @Test
    void sellStock_ejerIkkeAktien_rollback() {
        // Arrange — ingen ejede aktier
        stockDao.stock         = new Stock("AAPL", "Apple", 100.0, "Steady");
        portfolioDao.portfolio = new Portfolio(1, 5000.0);

        // Act
        service.sellStock(1, "AAPL", 5);

        // Assert
        assertTrue(uow.rolledBack);
    }
}
