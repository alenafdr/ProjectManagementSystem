package service;

import model.Core;

import java.util.List;

public interface GenericDAO {
    public abstract boolean save(Core core);
    public abstract Core getById(int id);
    public abstract List<Core> getAll();
    public abstract boolean update(int id, Core core);
    public abstract boolean remove(Core core);
}
