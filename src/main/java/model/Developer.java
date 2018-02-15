package model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Developer extends Core {


    private Set<Skill> skills;

    public Developer(String firstName) {
        super(firstName);
        skills = new HashSet<>();
    }

    public Developer(int id, String firstName) {
        super(id, firstName);
        skills = new HashSet<>();
    }

    public String getFirstName() {
        return super.getName();
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkill(Skill skill) {
        this.skills.add(skill);
    }

    public String getSkillsString(){
        if (skills.isEmpty()) return "0";
        StringBuilder result = new StringBuilder();
        for (Skill skill : skills){
            result.append(skill.getId() + ",");
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", \n\t\t\tskills=\n");
        for (Skill skill : skills){
            stringBuilder.append("\t\t\t\t" + skill.toString() + "\n");
        }
        return "Developer " +
                "id=" + super.getId() +
                ", firstName='" + getFirstName() + '\'' +
                stringBuilder.toString();
    }
}
