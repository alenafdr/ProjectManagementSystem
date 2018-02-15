package service;

import model.Developer;

import java.util.List;

public class DeveloperService implements GenericService<Developer, Integer> {

    JDBCDeveloperDAO jdbcDeveloperDAO = new JDBCDeveloperDAO();

    @Override
    public boolean save(Developer developer) {
        return jdbcDeveloperDAO.save(developer);
    }

    @Override
    public Developer getById(Integer integer) {
        return jdbcDeveloperDAO.getById(integer);
    }

    @Override
    public List<Developer> getAll() {
        return jdbcDeveloperDAO.getAll();
    }

    @Override
    public boolean update(Integer integer, Developer developer) {
        return update(integer, developer);
    }

    @Override
    public boolean remove(Developer developer) {
        return jdbcDeveloperDAO.remove(developer);
    }
}
