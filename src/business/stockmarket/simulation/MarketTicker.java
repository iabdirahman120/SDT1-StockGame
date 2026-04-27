package business.stockmarket.simulation;

import business.events.StockUpdateEvent;
import utility.Logger;

public class MarketTicker implements Runnable {

    // run() kører når tråden startes
    @Override
    public void run() {
        while (true) { // kører for evigt
            try {
                for (LiveStock stock : StockMarket.getInstance().getStocks()) {
                    stock.updatePrice(); // opdater prisen baseret på state

                    // opret event med aktuel data
                    StockUpdateEvent event = new StockUpdateEvent(
                            stock.getSymbol(),
                            stock.getCurrentPrice(),
                            stock.getCurrentStateName()
                    );

                    // notificer alle lyttere om opdateringen
                    StockMarket.getInstance().notifyListeners(event);
                }
                Thread.sleep(1000); // vent 1 sekund mellem opdateringer
            } catch (InterruptedException e) {
                Logger.getInstance().log("MarketTicker stoppet: " + e.getMessage());
            }
        }
    }

}
