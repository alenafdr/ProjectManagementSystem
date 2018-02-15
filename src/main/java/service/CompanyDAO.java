package service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import model.Company;
import model.Core;
import model.Developer;
import model.Project;
import view.ConsoleHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CompanyDAO extends CoreDAO {
    private String nameTable = "companies";
    ProjectDAO projectDAO;

    public CompanyDAO() {
        projectDAO = new ProjectDAO();
    }

    @Override
    public boolean save(Core core) {
        Company company = (Company) core;

        String sql = "INSERT INTO " + nameTable + "(name) VALUES " +
                "('" + company.getName() + "')"
                ;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as id");
            int idCompany = 0;
            while (resultSet.next()) {
                idCompany = resultSet.getInt("id");
                ConsoleHelper.showMessage("Inserted object (id=" + idCompany + ")");
            }
            for (Project project : company.getProjects()){
                projectDAO.addProjectToCompany(project.getId(), idCompany);
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
    public Company getById(int id){
        Company answer = null;
        if (id == 0){
            return new Company(0, "null");
        }
        String sql = "SELECT * FROM " + nameTable + " WHERE id = " + id;
        try(Connection connection = connect();
            Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int newId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                answer = new Company(newId, name);
                for (Project project : projectDAO.getProjectByCompabyId(newId)){
                    answer.setProject(project);
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    public List<Core> getAll(){
        List<Core> result = new ArrayList<>();
        Company company = null;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM " + nameTable + " ORDER BY name";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                company = new Company(id, name);
                for (Project project : projectDAO.getProjectByCompabyId(id)){
                    company.setProject(project);
                }
                result.add(company);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public boolean update(int id, Core core) {
        Company company = (Company) core;

        String sql = "UPDATE " + nameTable + " SET name = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, company.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
            for (Project project : company.getProjects()){
                projectDAO.addProjectToCompany(project.getId(), id);
            }
            company.setId(id);
            projectDAO.deleteAllProjectFromCompanyExcept(company);
            return true;
        } catch (MySQLIntegrityConstraintViolationException e){
            ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean remove(Core core) {
        Company company = (Company) core;

        String sql = "DELETE FROM " + nameTable + " WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, company.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }

        return false;
    }

}
