package service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import model.*;
import view.ConsoleHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JDBCProjectDAO extends JDBCGeneric implements GenericDAO{
    private String nameTable = "projects";
    JDBCDeveloperDAO JDBCDeveloperDAO;


    public JDBCProjectDAO() {
        JDBCDeveloperDAO = new JDBCDeveloperDAO();
    }

    @Override
    public boolean save(Core core){
        Project project = (Project) core;

        String sql = "INSERT INTO " + nameTable + "(name) VALUES " +
                "('" + project.getName() + "')"
                ;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as id");
            int idProject = 0;
            while (resultSet.next()) {
                idProject = resultSet.getInt("id");
                ConsoleHelper.showMessage("Inserted object (id=" + idProject + ")");
            }
            for (Developer developer : project.getDevelopers()){
                JDBCDeveloperDAO.addDeveloperToProject(developer.getId(), idProject);
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
    public Project getById(int id){
        Project result = null;
        if (id == 0){
            return new Project(0, "null");
        }
        String sql = "SELECT * FROM " + nameTable + " WHERE id = " + id;
        try(Connection connection = connect();
            Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int newId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                result = new Project(newId, name);
                for (Developer developer : JDBCDeveloperDAO.getDeveloperByProjectId(newId)){
                    result.setDeveloper(developer);
                }
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
        Project project = null;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM " + nameTable + " ORDER BY name";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                project = new Project(id, name);
                for (Developer developer : JDBCDeveloperDAO.getDeveloperByProjectId(id)){
                    project.setDeveloper(developer);
                }
                result.add(project);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(int id, Core core){
        Project project = (Project) core;

        String sql = "UPDATE " + nameTable + " SET name = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, project.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
            for (Developer developer : project.getDevelopers()){
                JDBCDeveloperDAO.addDeveloperToProject(developer.getId(), id);
            }
            project.setId(id);
            JDBCDeveloperDAO.deleteAllExcept(project);
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
        Project project = (Project) core;

        String sql = "DELETE FROM " + nameTable + " WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, project.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }

        return false;
    }

    public void addProjectToCompany(int idProject, int idCompany){
        String sql = "INSERT INTO companies_projects(company_id, project_id) " +
                "VALUES (" + idCompany + "," + idProject + ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (MySQLIntegrityConstraintViolationException e){
            //ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConsoleHelper.showMessage("Inserted project " + idProject + " to company " + idCompany);
    }

    public Set<Project> getProjectByCompabyId(int id){
        Set<Project> result = new HashSet<>();

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM companies_projects "+ " WHERE company_id = " + id+ " ORDER BY id";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int idNew = resultSet.getInt("project_id");
                result.add(getById(idNew));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //удалить все developer, кроме переданных

    public void deleteAllProjectFromCompanyExcept(Company company){

        String sql = "DELETE FROM companies_projects " +
                "WHERE company_id = " + company.getId() + " " +
                "AND project_id NOT IN (" + company.getProjectsString() + ")";
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProjectToCustomer(int idProject, int idCustomer){
        String sql = "INSERT INTO customers_projects(customer_id, project_id) " +
                "VALUES (" + idCustomer + "," + idProject + ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (MySQLIntegrityConstraintViolationException e){
            //ConsoleHelper.showMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConsoleHelper.showMessage("Inserted project " + idProject + " to customer " + idCustomer);
    }

    public Set<Project> getProjectByCustomerId(int id){
        Set<Project> result = new HashSet<>();

        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM customers_projects "+ " WHERE customer_id = " + id+ " ORDER BY id";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int idNew = resultSet.getInt("project_id");
                result.add(getById(idNew));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //удалить все developer, кроме переданных

    public void deleteAllProjectFromCustomerExcept(Customer customer){

        String sql = "DELETE FROM customers_projects " +
                "WHERE customer_id = " + customer.getId() + " " +
                "AND project_id NOT IN (" + customer.getProjectsString() + ")";
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
