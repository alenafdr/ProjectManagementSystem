package controller;

import model.Core;
import model.Developer;
import model.Skill;
import service.DeveloperDAO;
import service.SkillDAO;
import view.ConsoleHelper;
import view.CoreView;

import java.math.BigDecimal;
import java.util.List;

public class DeveloperController extends CoreController {
    private DeveloperDAO developerDAO;
    private SkillDAO skillDAO;
    private DataReceiver dr;

    public DeveloperController() {
        super();
        dr = super.getDr();
        developerDAO = new DeveloperDAO();
        skillDAO = new SkillDAO();
        super.start();
    }

    @Override
    public Developer create(){

        ConsoleHelper.showMessage("Введите name для нового объекта");
        String firstName = dr.readString();

        Developer developer = new Developer(firstName);

        //после создания объекта заполняем скилы
        int idSkill;

        do {
            ConsoleHelper.showMessage("Введите id skill для нового объекта или 0, чтобы продолжить");

            Skill skill;
            for (Core core : skillDAO.getAll()){ //показать все навыки, которые есть в базе
                skill = (Skill) core;
                CoreView.show(skill);
            }

            idSkill = dr.readInt();
            if (idSkill == 0) continue;
            if (skillDAO.getById(idSkill) == null){
                ConsoleHelper.showMessage("Skill с таким id не существует, перейти в меню сущности skill? yes/no");
                if (dr.readBoolean()){
                    new SkillController(); //переходим в меню skill
                } else {
                    continue;
                }
            }
            developer.setSkill(skillDAO.getById(idSkill));
            ConsoleHelper.showMessage("Добавлен skill " + skillDAO.getById(idSkill).toString());

        } while (idSkill != 0);
        return developer;
    }

    @Override
    public void save(Core core) {
        Developer developer = (Developer) core;

        if (developerDAO.save(developer)){
            ConsoleHelper.showMessage("Объект создан");
        } else {
            ConsoleHelper.showMessage("Объект не создан, попробуйте еще раз");
        }
    }

    @Override
    public void read(){
        do{
            ConsoleHelper.showMessage("Введите id объекта или 0 чтобы выйти из этого меню");
            int id = dr.readInt();
            if (id == 0) {
                break;
            }
            Developer developer = developerDAO.getById(id);
            if (developer != null){
                CoreView.show(developer);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    @Override
    public void readAll(){
        List<Core> developers = developerDAO.getAll();
        if (developers.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Developer developer;
            for (Core core : developers){
                developer = (Developer) core;
                CoreView.show(developer);
            }
        }
    }

    @Override
    public void update(){
        Developer developer;
        int id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readInt();
            if (id == 0) return;
            developer = developerDAO.getById(id);
            if (developer == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (developer == null);


        if (developerDAO.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект");
        }
    }

    @Override
    public void delete(){
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        int id = dr.readInt();

        if (developerDAO.remove(developerDAO.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
