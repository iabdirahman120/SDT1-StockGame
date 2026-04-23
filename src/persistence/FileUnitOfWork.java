package persistence;

import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import utility.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUnitOfWork implements UnitOfWork {
    private String directory;
    private List<Stock> stocks;
    private List<Portfolio> portfolios;
    private List<OwnedStock> ownedStocks;

    public FileUnitOfWork(String directory) {
        this.directory = directory;
    }

    @Override
    public void beginTransaction() {
        stocks = null;
        portfolios = null;
        ownedStocks = null;
    }

    @Override
    public void commit() {
        try {
            writeStocksToFile();
            writePortfoliosToFile();
            writeOwnedStocksToFile();
        } catch (Exception e) {
            Logger.getInstance().log("Fejl ved commit: " + e.getMessage());
        }
    }

    @Override
    public void rollback() {
        stocks = null;
        portfolios = null;
        ownedStocks = null;
    }

    public List<Stock> getStocks() {
        if (stocks == null) {
            stocks = readStocksFromFile();
        }
        return stocks;
    }

    public List<Portfolio> getPortfolios() {
        if (portfolios == null) {
            portfolios = readPortfoliosFromFile();
        }
        return portfolios;
    }

    private List<Portfolio> readPortfoliosFromFile() {
        try {
            List<Portfolio> result = new ArrayList<>();
            List<String> lines = Files.readAllLines(Path.of(directory + "/portfolios.txt"));
            for (String line : lines) {
                String[] parts = line.split("\\|");
                result.add(new Portfolio(Integer.parseInt(parts[0]), Double.parseDouble(parts[1])));
            }
            return result;
        } catch (Exception e) {
            Logger.getInstance().log("Fejl ved læsning: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    private List<Stock> readStocksFromFile() {
        try {
            List<Stock> result = new ArrayList<>();
            List<String> lines = Files.readAllLines(Path.of(directory + "/stocks.txt"));
            for (String line : lines) {
                String[] parts = line.split("\\|");
                result.add(new Stock(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]));
            }
            return result;
        } catch (Exception e) {
            Logger.getInstance().log("Fejl ved læsning: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<OwnedStock> getOwnedStocks() {
        if (ownedStocks == null) {
            ownedStocks = readOwnedStocksFromFile();
        }
        return ownedStocks;
    }

    private List<OwnedStock> readOwnedStocksFromFile() {
        try {
            List<OwnedStock> result = new ArrayList<>();
            List<String> lines = Files.readAllLines(Path.of(directory + "/ownedstocks.txt"));
            for (String line : lines) {
                String[] parts = line.split("\\|");
                result.add(new OwnedStock(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3])));
            }
            return result;
        } catch (Exception e) {
            Logger.getInstance().log("Fejl ved læsning: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    private void writeStocksToFile() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Stock stock : stocks) {
            sb.append(stock.getSymbol()).append("|")
                    .append(stock.getName()).append("|")
                    .append(stock.getCurrentPrice()).append("|")
                    .append(stock.getCurrentState()).append("\n");
        }
        Files.writeString(Path.of(directory + "/stocks.txt"), sb.toString());
    }

    private void writePortfoliosToFile() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Portfolio p : portfolios) {
            sb.append(p.getId()).append("|")
                    .append(p.getCurrentBalance()).append("\n");
        }
        Files.writeString(Path.of(directory + "/portfolios.txt"), sb.toString());
    }

    private void writeOwnedStocksToFile() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (OwnedStock os : ownedStocks) {
            sb.append(os.getId()).append("|")
                    .append(os.getPortfolioId()).append("|")
                    .append(os.getStockSymbol()).append("|")
                    .append(os.getNumberOfShares()).append("\n");
        }
        Files.writeString(Path.of(directory + "/ownedstocks.txt"), sb.toString());
    }
}
