package controller;

import model.Core;
import model.Skill;
import service.SkillDAO;
import view.ConsoleHelper;
import view.CoreView;

import java.util.List;

public class SkillController extends CoreController {
    private SkillDAO skillDAO;
    private DataReceiver dr;

    public SkillController() {
        skillDAO = new SkillDAO();
        dr = new DataReceiver();
        super.start();
    }

    @Override
    public Skill create() {

        ConsoleHelper.showMessage("Введите name для нового объекта");
        String name = dr.readString();

        Skill skill = new Skill(name);

        return skill;
    }

    @Override
    public void save(Core core) {
        Skill skill = (Skill) core;

        if (skillDAO.save(skill)){
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
            Skill skill = skillDAO.getById(id);
            if (skill != null){
                CoreView.show(skill);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    @Override
    public void readAll() {
        List<Core> skills = skillDAO.getAll();
        if (skills.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Skill skill;
            for (Core core : skills){
                skill = (Skill) core;
                CoreView.show(skill);
            }
        }
    }

    @Override
    public void update() {
        Skill skill;
        int id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readInt();
            if (id == 0) return;
            skill = skillDAO.getById(id);
            if (skill == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (skill == null);

        if (skillDAO.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект");
        }

    }

    @Override
    public void delete() {
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        int id = dr.readInt();

        if (skillDAO.remove(skillDAO.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
