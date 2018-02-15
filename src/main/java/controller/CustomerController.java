package controller;

import model.Core;
import model.Customer;
import model.Project;
import service.JDBCCustomerDAO;
import service.JDBCProjectDAO;
import view.ConsoleHelper;
import view.CoreView;

import java.util.List;

public class CustomerController extends CoreController {
    private DataReceiver dr;
    private JDBCProjectDAO JDBCProjectDAO;
    private JDBCCustomerDAO JDBCCustomerDAO;

    public CustomerController() {
        super();
        dr = super.getDr();
        JDBCProjectDAO = new JDBCProjectDAO();
        JDBCCustomerDAO = new JDBCCustomerDAO();
        super.start();
    }

    @Override
    public Customer create() {

        ConsoleHelper.showMessage("Введите name для нового объекта");
        String name = dr.readString();

        Customer customer = new Customer(name);

        //после создания объекта заполняем команды
        int idProject;

        do {
            ConsoleHelper.showMessage("Введите id project для нового объекта или 0, чтобы продолжить");

            Project project;
            for (Core core : JDBCProjectDAO.getAll()){ //показать все проекты, которые есть в базе
                project = (Project) core;
                CoreView.show(project);
            }

            idProject = dr.readInt();
            if (idProject == 0) continue;
            if (JDBCProjectDAO.getById(idProject) == null){
                ConsoleHelper.showMessage("Project с таким id не существует, перейти в меню сущности project? yes/no");
                if (dr.readBoolean()){
                    ProjectController projectController = new ProjectController(); //переходим в меню skill
                } else {
                    continue;
                }
            }
            customer.setProject(JDBCProjectDAO.getById(idProject));
            ConsoleHelper.showMessage("Добавлен project " + JDBCProjectDAO.getById(idProject));

        } while (idProject != 0);

        return customer;
    }

    @Override
    public void save(Core core) {
        Customer customer = (Customer) core;

        if (JDBCCustomerDAO.save(customer)){
            ConsoleHelper.showMessage("Объект создан");
        } else {
            ConsoleHelper.showMessage("Объект не создан, попробуйте еще раз");
        }
    }

    @Override
    public void read() {
        do{
            ConsoleHelper.showMessage("Введите id объекта или 0 чтобы выйти из этого меню");
            int id = dr.readInt();
            if (id == 0) {
                break;
            }
            Customer customer = JDBCCustomerDAO.getById(id);
            if (customer != null){
                CoreView.show(customer);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    @Override
    public void readAll() {
        List<Core> customers = JDBCCustomerDAO.getAll();
        if (customers.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Customer customer;
            for (Core core : customers){
                customer = (Customer) core;
                CoreView.show(customer);
            }
        }
    }

    @Override
    public void update() {
        Customer customer;
        int id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readInt();
            if (id == 0) return;
            customer = JDBCCustomerDAO.getById(id);
            if (customer == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (customer == null);

        if (JDBCCustomerDAO.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект");
        }

    }

    @Override
    public void delete() {
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        int id = dr.readInt();

        if (JDBCCustomerDAO.remove(JDBCCustomerDAO.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
