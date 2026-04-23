package persistence;

import java.util.List;
import domain.Stock;

public interface StockDao {
    Stock getById(String symbol);
    List<Stock> getAll();
    void create (Stock stock);
    void update (Stock stock);
    void delete (String symbol);
}
