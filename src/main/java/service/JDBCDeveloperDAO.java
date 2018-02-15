package service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import model.Developer;
import model.Project;
import model.Skill;
import view.ConsoleHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JDBCDeveloperDAO extends JDBCGeneric implements GenericDAO<Developer, Integer>{
    private String nameTable = "developers";
    private JDBCSkillDAO JDBCSkillDAO;

    public JDBCDeveloperDAO() {
        JDBCSkillDAO = new JDBCSkillDAO();
    }

    @Override
    public boolean save(Developer developer){
        String sql = "INSERT INTO " + nameTable + "(name) VALUES " +
                "('" + developer.getName() + "')"
                ;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as id");
            int idDeveloper = 0;
            while (resultSet.next()) {
                idDeveloper = resultSet.getInt("id");
                ConsoleHelper.showMessage("Inserted object (id=" + idDeveloper + ")");
            }
            for (Skill skill : developer.getSkills()){
                JDBCSkillDAO.addSkillToDeveloper(skill.getId(), idDeveloper);
            }
            resultSet.close();
            return true;
        } catch (MySQLIntegrityConstraintViolationException e){
            ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Developer getById(Integer id){
        Developer result = null;
        if (id == 0){
            return new Developer(0, "null");
        }
        String sql = "SELECT * FROM " + nameTable + " WHERE id = " + id;
        try(Connection connection = connect();
            Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int newId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                result = new Developer(newId, name);
                for (Skill skill : JDBCSkillDAO.getSkillByDeveloperId(newId)){
                    result.setSkill(skill);
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Developer> getAll(){
        List<Developer> result = new ArrayList<>();
        Developer developer = null;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM " + nameTable + " ORDER BY name";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                developer = new Developer(id, name);
                for (Skill skill : JDBCSkillDAO.getSkillByDeveloperId(id)){
                    developer.setSkill(skill);
                }
                result.add(developer);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(Integer id, Developer developer){
        String sql = "UPDATE " + nameTable + " SET name = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, developer.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
            for (Skill skill : developer.getSkills()){
                JDBCSkillDAO.addSkillToDeveloper(skill.getId(), id);
            }
            developer.setId(id);
            JDBCSkillDAO.deleteAllExcept(developer);
            return true;
        } catch (MySQLIntegrityConstraintViolationException e){
            ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean remove(Developer developer){
        String sql = "DELETE FROM " + nameTable + " WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, developer.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }

        return false;
    }

    public void addDeveloperToProject(int idDeveloper, int idProject){
        String sql = "INSERT INTO projects_developers(project_id, developer_id) " +
                "VALUES (" + idProject + "," + idDeveloper + ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (MySQLIntegrityConstraintViolationException e){
            //ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConsoleHelper.showMessage("Inserted developer " + idDeveloper + " to project " + idProject);
    }

    public Set<Developer> getDeveloperByProjectId(int id){
        Set<Developer> result = new HashSet<>();

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM projects_developers "+ " WHERE project_id = " + id+ " ORDER BY id";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int idNew = resultSet.getInt("developer_id");
                result.add(getById(idNew));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //удалить все developer, кроме переданных

    public void deleteAllExcept(Project project){

        String sql = "DELETE FROM projects_developers " +
                "WHERE project_id = " + project.getId() + " " +
                "AND developer_id NOT IN (" + project.getDevelopersString() + ")";
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
