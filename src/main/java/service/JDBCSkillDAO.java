package service;


import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import model.Core;
import model.Developer;
import model.Skill;
import view.ConsoleHelper;

import java.sql.*;
import java.util.*;

public class JDBCSkillDAO extends JDBCGeneric implements GenericDAO{

    private String nameTable = "skills";

    @Override
    public boolean save(Core core){
        Skill skill = (Skill) core;

        String sql = "INSERT INTO " + nameTable + "(name) VALUES ('" + skill.getName() + "')";
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as id");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                ConsoleHelper.showMessage("Insert object (id=" + id + ")");
                return true;
            }
            resultSet.close();

        } catch (MySQLIntegrityConstraintViolationException e){
            ConsoleHelper.showMessage("Duplicate entry '" + skill.getName() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Skill getById(int id){
        Skill result = null;
        if (id == 0){
            return new Skill(0, "no skill");
        }
        String sql = "SELECT * FROM " + nameTable + " WHERE id = " + id;
        try(Connection connection = connect();
            Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int newId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                result = new Skill(newId, name);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Core> getAll(){
        List<Core> result = new ArrayList<>();
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM " + nameTable + " ORDER BY id";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                result.add(new Skill(id, name));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean update(int id, Core core){
        Skill skill = (Skill) core;

        String sql = "UPDATE " + nameTable + " SET name = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, skill.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
            return true;
        } catch (MySQLIntegrityConstraintViolationException e){
            ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean remove(Core core){
        Skill skill = (Skill) core;

        String sql = "DELETE FROM " + nameTable + " WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, skill.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            ConsoleHelper.showMessage("Нет такого объекта");
        }

        return false;
    }

    public void addSkillToDeveloper(int idSkill, int idDeveloper){
        String sql = "INSERT INTO developers_skills(developer_id, skill_id) " +
                "VALUES (" + idDeveloper + "," + idSkill + ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (MySQLIntegrityConstraintViolationException e){
            //ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConsoleHelper.showMessage("Inserted skill " + idSkill + " to developer " + idDeveloper);
    }

    public Set<Skill> getSkillByDeveloperId(int id){
        Set<Skill> result = new HashSet<>();

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM developers_skills "+ " WHERE developer_id = " + id+ " ORDER BY id";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int idNew = resultSet.getInt("skill_id");
                result.add(getById(idNew));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //удалить все skill, кроме переданных

    public void deleteAllExcept(Developer developer){

        String sql = "DELETE FROM developers_skills " +
                "WHERE developer_id = " + developer.getId() + " " +
                "AND skill_id NOT IN (" + developer.getSkillsString() + ")";
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
