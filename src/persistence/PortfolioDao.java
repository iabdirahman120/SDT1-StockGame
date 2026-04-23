package persistence;

import domain.Portfolio;
import java.util.List;

public interface PortfolioDao {

        Portfolio getById(int id);
        List<Portfolio> getAll();
        void create (Portfolio portfolio);
        void update (Portfolio portfolio);
        void delete (int id);
    }

