package controller;

import model.Core;
import model.Developer;
import model.Project;
import service.DeveloperService;
import service.ProjectService;
import view.ConsoleHelper;
import view.CoreView;

import java.util.List;

public class ProjectController extends CoreController {
    private DataReceiver dr;
    private DeveloperService developerService;
    private ProjectService projectService;

    public ProjectController() {
        super();
        dr = super.getDr();
        developerService = new DeveloperService();
        projectService = new ProjectService();
        start();
    }

    public Project create(){
        ConsoleHelper.showMessage("Введите name для нового объекта");
        String name = dr.readString();

        Project project = new Project(name);

        //после создания объекта заполняем команды
        int idTeam;

        do {
            ConsoleHelper.showMessage("Введите id developer для нового объекта или 0, чтобы продолжить");

            Developer developer;
            for (Core core : developerService.getAll()){ //показать все команды, которые есть в базе
                developer = (Developer) core;
                CoreView.show(developer);
            }

            idTeam = dr.readInt();
            if (idTeam == 0) continue;
            if (developerService.getById(idTeam) == null){
                ConsoleHelper.showMessage("Developer с таким id не существует, перейти в меню сущности developer? yes/no");
                if (dr.readBoolean()){
                    DeveloperController developerController = new DeveloperController(); //переходим в меню developer
                } else {
                    continue;
                }
            }
            project.setDeveloper(developerService.getById(idTeam));
            ConsoleHelper.showMessage("Добавлен developer " + developerService.getById(idTeam));

        } while (idTeam != 0);


        return project;
    }

    @Override
    public void save(Core core) {
        Project project = (Project) core;

        if (projectService.save(project)){
            ConsoleHelper.showMessage("Объект создан");
        } else {
            ConsoleHelper.showMessage("Объект не создан, попробуйте еще раз");
        }
    }

    public void read(){
        do{
            ConsoleHelper.showMessage("Введите id объекта или 0 чтобы выйти из этого меню");
            int id = dr.readInt();
            if (id == 0) {
                break;
            }
            Project project = projectService.getById(id);
            if (project != null){
                CoreView.show(project);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    public void readAll(){
        List<Project> projects = projectService.getAll();
        if (projects.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Project project;
            for (Core core : projects){
                project = (Project) core;
                CoreView.show(project);
            }
        }
    }

    public void update(){
        Project project;
        int id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readInt();
            if (id == 0) return;
            project = projectService.getById(id);
            if (project == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (project == null);

        if (projectService.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект");
        }
    }

    public void delete(){
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        int id = dr.readInt();

        if (projectService.remove(projectService.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
