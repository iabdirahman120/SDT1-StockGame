package business;

import business.events.StockUpdateEvent;
import business.stockmarket.simulation.StockListener;
import domain.Stock;
import persistence.FileUnitOfWork;
import persistence.FileStockDao;
import utility.Logger;

public class StockListenerService implements StockListener {
    private FileStockDao stockDao;
    private FileUnitOfWork uow;

    public StockListenerService(FileStockDao stockDao, FileUnitOfWork uow) {
        this.stockDao = stockDao;
        this.uow = uow;
    }

    @Override
    public void onStockUpdated(StockUpdateEvent event) {
        try {
            uow.beginTransaction();
            Stock stock = stockDao.getById(event.symbol());
            if (stock != null) {
                stock.setCurrentPrice(event.price());
                stock.setCurrentState(event.state());
                stockDao.update(stock);
                uow.commit();
            }
        } catch (Exception e) {
            Logger.getInstance().log("Fejl: " + e.getMessage());
            uow.rollback();
        }
    }
}
