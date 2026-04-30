import business.StockListenerService;
import business.TradingService;
import business.stockmarket.simulation.StockMarket;
import business.stockmarket.simulation.MarketTicker;
import domain.Stock;
import persistence.*;
import presentation.MainView;
import presentation.PortfolioViewModel;
import presentation.StockViewModel;

public class Main {

    public static void main(String[] args) throws Exception {

        // --- PERSISTENCE ---
        FileUnitOfWork uow = new FileUnitOfWork("data");
        FileStockDao stockDao    = new FileStockDao(uow);
        FilePortfolioDao portDao = new FilePortfolioDao(uow);
        FileOwnedStockDao osDao  = new FileOwnedStockDao(uow);

        // --- SERVICES ---
        TradingService tradingService = new TradingService(uow, stockDao, portDao, osDao);
        StockListenerService listenerService = new StockListenerService(stockDao, uow);



        // --- STOCK MARKET ---
        StockMarket market = StockMarket.getInstance();
        market.addListener(listenerService);

        // --- VIEW MODEL ---
        PortfolioViewModel portfolioVM = new PortfolioViewModel(tradingService, 1, 10000.0);
        market.addListener(portfolioVM);

        // Tilføj aktier til ViewModel
        for (Stock s : stockDao.getAll()) {
            portfolioVM.addStock(new StockViewModel(
                    s.getSymbol(), s.getName(), s.getCurrentPrice(), s.getCurrentState()
            ));
        }

        // --- START MARKET TICKER ---
        Thread tickerThread = new Thread(new MarketTicker());
        tickerThread.setDaemon(true);
        tickerThread.start();

        // --- START JAVAFX ---
        MainView.sharedViewModel = portfolioVM;
        javafx.application.Application.launch(MainView.class, args);
    }
}
