package business;

import business.strategy.BalancedStrategy;
import business.strategy.TradingStrategy;
import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import persistence.OwnedStockDao;
import persistence.PortfolioDao;
import persistence.StockDao;
import persistence.UnitOfWork;
import utility.Logger;

public class TradingService {

    private UnitOfWork uow;
    private StockDao stockDao;
    private PortfolioDao portfolioDao;
    private OwnedStockDao ownedStockDao;
    // Den valgte strategi — kan skiftes på runtime
    private TradingStrategy strategy = new BalancedStrategy();

    public void setStrategy(TradingStrategy strategy) {
        this.strategy = strategy;
    }

    // Køb aktier automatisk baseret på den valgte strategi
    public void buyWithStrategy(int portfolioId, String symbol) {
        try {
            Portfolio portfolio = portfolioDao.getById(portfolioId);
            Stock stock         = stockDao.getById(symbol);
            if (portfolio == null || stock == null) return;

            int quantity = strategy.calculateQuantity(
                    portfolio.getCurrentBalance(), stock.getCurrentPrice()
            );

            if (quantity > 0) {
                buyStock(portfolioId, symbol, quantity);
            }
        } catch (Exception e) {
            Logger.getInstance().log("Fejl ved strategy køb: " + e.getMessage());
        }
    }



    public TradingService(UnitOfWork uow, StockDao stockDao,
                          PortfolioDao portfolioDao, OwnedStockDao ownedStockDao) {
        this.uow = uow;
        this.stockDao = stockDao;
        this.portfolioDao = portfolioDao;
        this.ownedStockDao = ownedStockDao;
    }

    public void buyStock(int portfolioId, String symbol, int quantity) {
        try {
            uow.beginTransaction();
            if (quantity <= 0) throw new Exception("Antal skal være positivt");
            Stock stock = stockDao.getById(symbol);
            if (stock == null) throw new Exception("Aktie findes ikke");
            if (stock.getCurrentState().equals("Bankrupt")) throw new Exception("Aktie er konkurs");
            double fee = stock.getCurrentPrice() * quantity * 0.02;
            double totalCost = stock.getCurrentPrice() * quantity + fee;
            Portfolio portfolio = portfolioDao.getById(portfolioId);
            if (portfolio == null) throw new Exception("Portfolio findes ikke");
            if (portfolio.getCurrentBalance() < totalCost) throw new Exception("Ikke nok penge");
            portfolio.setCurrentBalance(portfolio.getCurrentBalance() - totalCost);
            portfolioDao.update(portfolio);
            OwnedStock existing = null;
            for (OwnedStock os : ownedStockDao.getAll()) {
                if (os.getPortfolioId() == portfolioId && os.getStockSymbol().equals(symbol)) {
                    existing = os;
                    break;
                }
            }
            if (existing != null) {
                existing.setNumberOfShares(existing.getNumberOfShares() + quantity);
                ownedStockDao.update(existing);
            } else {
                ownedStockDao.create(new OwnedStock(0, portfolioId, symbol, quantity));
            }
            uow.commit();
        } catch (Exception e) {
            Logger.getInstance().log("Fejl ved køb: " + e.getMessage());
            uow.rollback();
        }
    }

    public void sellStock(int portfolioId, String symbol, int quantity) {
        try {
            uow.beginTransaction();
            if (quantity <= 0) throw new Exception("Antal skal være positivt");
            OwnedStock owned = null;
            for (OwnedStock os : ownedStockDao.getAll()) {
                if (os.getPortfolioId() == portfolioId && os.getStockSymbol().equals(symbol)) {
                    owned = os;
                    break;
                }
            }
            if (owned == null) throw new Exception("Du ejer ikke denne aktie");
            if (owned.getNumberOfShares() < quantity) throw new Exception("Ikke nok aktier");
            Stock stock = stockDao.getById(symbol);
            double fee = stock.getCurrentPrice() * quantity * 0.02;
            double totalAmount = stock.getCurrentPrice() * quantity - fee;
            Portfolio portfolio = portfolioDao.getById(portfolioId);
            portfolio.setCurrentBalance(portfolio.getCurrentBalance() + totalAmount);
            portfolioDao.update(portfolio);
            if (owned.getNumberOfShares() == quantity) {
                ownedStockDao.delete(owned.getId());
            } else {
                owned.setNumberOfShares(owned.getNumberOfShares() - quantity);
                ownedStockDao.update(owned);
            }
            uow.commit();
        } catch (Exception e) {
            Logger.getInstance().log("Fejl ved salg: " + e.getMessage());
            uow.rollback();
        }
    }
}
