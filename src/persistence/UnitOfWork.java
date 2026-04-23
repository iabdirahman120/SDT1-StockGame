package persistence;


public interface UnitOfWork {
    void beginTransaction();
    void commit();
    void rollback();
}
