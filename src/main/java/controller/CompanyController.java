package controller;

import model.Company;
import model.Core;
import model.Project;
import service.JDBCCompanyDAO;
import service.JDBCProjectDAO;
import view.ConsoleHelper;
import view.CoreView;

import java.util.List;

public class CompanyController extends CoreController {
    private DataReceiver dr;
    private JDBCProjectDAO JDBCProjectDAO;
    private JDBCCompanyDAO companyDAO;

    public CompanyController() {
        super();
        dr = super.getDr();
        JDBCProjectDAO = new JDBCProjectDAO();
        companyDAO = new JDBCCompanyDAO();
        super.start();
    }

    @Override
    public Company create() {

        ConsoleHelper.showMessage("Введите name для нового объекта");
        String name = dr.readString();

        Company company = new Company(name);

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
            company.setProject(JDBCProjectDAO.getById(idProject));
            ConsoleHelper.showMessage("Добавлен project " + JDBCProjectDAO.getById(idProject));

        } while (idProject != 0);

        return company;
    }

    @Override
    public void save(Core core) {
        Company company = (Company) core;

        if (companyDAO.save(company)){
            ConsoleHelper.showMessage("Объект сохранен");
        } else {
            ConsoleHelper.showMessage("Объект не сохранен, попробуйте еще раз");
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
            Company company = companyDAO.getById(id);
            if (company != null){
                CoreView.show(company);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    @Override
    public void readAll() {
        List<Core> companies = companyDAO.getAll();
        if (companies.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Company company;
            for (Core core : companies){
                company = (Company) core;
                CoreView.show(company);
            }
        }
    }

    @Override
    public void update() {
        Company company;
        int id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readInt();
            if (id == 0) return;
            company = companyDAO.getById(id);
            if (company == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (company == null);

        if (companyDAO.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект");

        }
    }

    @Override
    public void delete() {
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        int id = dr.readInt();

        if (companyDAO.remove(companyDAO.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
