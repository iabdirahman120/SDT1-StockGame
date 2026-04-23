package persistence;

import domain.Stock;

import java.util.List;

public class FileStockDao implements StockDao {
    private FileUnitOfWork uow;

    public FileStockDao(FileUnitOfWork uow){
        this.uow=uow;
    }



    @Override
    public Stock getById(String symbol) {
        for(Stock stock : uow.getStocks()) {
            if(stock.getSymbol().equals(symbol)){
                return stock;
            }
        }
        return null;
    }

    @Override
    public List<Stock> getAll() {
        return uow.getStocks();
    }

    @Override
    public void create(Stock stock) {
        uow.getStocks().add(stock);

    }

    @Override
    public void update(Stock stock) {
        List<Stock> stocks = uow.getStocks();
        for(int i=0; i<stocks.size(); i++){
            if (stocks.get(i).getSymbol().equals(stock.getSymbol())){
                stocks.set(i,stock);
                return;
            }
        }

    }

    @Override
    public void delete(String symbol) {
        uow.getStocks().removeIf(s->s.getSymbol().equals(symbol));

    }
}
