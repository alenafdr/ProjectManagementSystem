package model;

public class Skill extends Core {

    public Skill(String name) {
        super(name);
    }

    public Skill(int id, String name) {
        super(id,name);
    }

    @Override
    public String toString() {
        return "Skill " +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'';
    }
}
