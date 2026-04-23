package persistence;

import domain.Portfolio;
import java.util.List;

public class FilePortfolioDao implements PortfolioDao {
    private FileUnitOfWork uow;

    public FilePortfolioDao(FileUnitOfWork uow){
        this.uow=uow;
    }



    @Override
    public Portfolio getById(int id) {
        for(Portfolio p : uow.getPortfolios()) {
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Portfolio> getAll() {
        return uow.getPortfolios();
    }

    @Override
    public void create(Portfolio portfolio) {
        uow.getPortfolios().add(portfolio);

    }

    @Override
    public void update(Portfolio portfolio) {
        List<Portfolio> portfolios = uow.getPortfolios();
        for(int i=0; i<portfolios.size(); i++){
            if (portfolios.get(i).getId() == portfolio.getId()) {
                portfolios.set(i,portfolio);
                return;
            }
        }

    }

    @Override
    public void delete(int id) {
        uow.getPortfolios().removeIf(p->p.getId()==(id));

    }
}
