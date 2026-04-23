package persistence;

import domain.Portfolio;

import java.util.List;
import domain.OwnedStock;

public interface OwnedStockDao {

        OwnedStock getById(int id);
        List<OwnedStock> getAll();
        void create (OwnedStock ownedStock);
        void update (OwnedStock ownedStock);
        void delete (int id);
    }



