package service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import model.Core;
import model.Customer;
import model.Project;
import view.ConsoleHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCCustomerDAO extends JDBCGeneric implements GenericDAO{
    private String nameTable = "customers";
    JDBCProjectDAO JDBCProjectDAO;

    public JDBCCustomerDAO() {
        JDBCProjectDAO = new JDBCProjectDAO();
    }

    @Override
    public boolean save(Core core) {
        Customer customer = (Customer) core;
        String sql = "INSERT INTO " + nameTable + "(name) VALUES " +
                "('" + customer.getName() + "')"
                ;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as id");
            int idCustomer = 0;
            while (resultSet.next()) {
                idCustomer = resultSet.getInt("id");
                ConsoleHelper.showMessage("Inserted object (id=" + idCustomer + ")");
            }
            for (Project project : customer.getProjects()){
                JDBCProjectDAO.addProjectToCustomer(project.getId(), idCustomer);
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
    public Customer getById(int id) {
        Customer result = null;
        if (id == 0){
            return new Customer(0, "null");
        }
        String sql = "SELECT * FROM " + nameTable + " WHERE id = " + id;
        try(Connection connection = connect();
            Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int newId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                result = new Customer(newId, name);
                for (Project project : JDBCProjectDAO.getProjectByCustomerId(newId)){
                    result.setProject(project);
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public List<Core> getAll() {
        List<Core> result = new ArrayList<>();
        Customer customer = null;
        try (Connection connection = connect();
             Statement statement = connection.createStatement())
        {
            String sql = "SELECT * FROM " + nameTable + " ORDER BY name";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                customer = new Customer(id, name);
                for (Project project : JDBCProjectDAO.getProjectByCustomerId(id)){
                    customer.setProject(project);
                }
                result.add(customer);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(int id, Core core) {
        Customer customer = (Customer) core;
        String sql = "UPDATE " + nameTable + " SET name = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, customer.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
            for (Project project : customer.getProjects()){
                JDBCProjectDAO.addProjectToCustomer(project.getId(), id);
            }
            customer.setId(id);
            JDBCProjectDAO.deleteAllProjectFromCustomerExcept(customer);
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
        Customer customer = (Customer) core;

        String sql = "DELETE FROM " + nameTable + " WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, customer.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }

        return false;
    }

}
