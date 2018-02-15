package service;

import model.Project;

import java.util.List;

public class ProjectService implements GenericService<Project, Integer> {
    JDBCProjectDAO jdbcProjectDAO = new JDBCProjectDAO();

    @Override
    public boolean save(Project project) {
        return jdbcProjectDAO.save(project);
    }

    @Override
    public Project getById(Integer integer) {
        return jdbcProjectDAO.getById(integer);
    }

    @Override
    public List<Project> getAll() {
        return jdbcProjectDAO.getAll();
    }

    @Override
    public boolean update(Integer integer, Project project) {
        return jdbcProjectDAO.update(integer, project);
    }

    @Override
    public boolean remove(Project project) {
        return jdbcProjectDAO.remove(project);
    }
}
