package business.stockmarket.simulation;

import utility.Logger;

public class MarketTicker implements Runnable {

    // run() kører når tråden startes
    @Override
    public void run() {
        while (true) { // kører for evigt
            try {
                // opdater prisen på alle aktier
                for (LiveStock stock : StockMarket.getInstance().getStocks()) {
                    stock.updatePrice();
                }
                Thread.sleep(1000); // vent 1 sekund mellem hver opdatering
            } catch (InterruptedException e) {
                Logger.getInstance().log("MarketTicker stoppet: " + e.getMessage());
            }
        }
    }
}
