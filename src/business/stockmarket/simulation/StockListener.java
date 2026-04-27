package business.stockmarket.simulation;

import business.events.StockUpdateEvent;

public interface StockListener {
    void onStockUpdated(StockUpdateEvent event);
}
