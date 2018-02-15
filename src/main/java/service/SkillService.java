package service;

import model.Skill;

import java.util.List;

public class SkillService implements GenericService<Skill, Integer> {
    private JDBCSkillDAO jdbcSkillDAO = new JDBCSkillDAO();

    @Override
    public boolean save(Skill skill) {
        return jdbcSkillDAO.save(skill);
    }

    @Override
    public Skill getById(Integer id) {
        return jdbcSkillDAO.getById(id);
    }

    @Override
    public List<Skill> getAll() {
        return jdbcSkillDAO.getAll();
    }

    @Override
    public boolean update(Integer id, Skill skill) {
        return jdbcSkillDAO.update(id, skill);
    }

    @Override
    public boolean remove(Skill skill) {
        return jdbcSkillDAO.remove(skill);
    }
}
