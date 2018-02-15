package model;

import java.util.HashSet;
import java.util.Set;

public class Project extends Core {
    private Set<Developer> developers;

    public Project(String name) {
        super(name);
        developers = new HashSet<>();
    }

    public Project(int id, String name) {
        super(id, name);
        developers = new HashSet<>();
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public void setDeveloper(Developer developer) {
        this.developers.add(developer);
    }

    public String getDevelopersString(){
        if (developers.isEmpty()) return "0";
        StringBuilder result = new StringBuilder();
        for (Developer developer : developers){
            result.append(developer.getId() + ",");
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", \n\tdevelopers=\n");
        for (Developer developer : developers){
            stringBuilder.append("\t\t" + developer.toString() + "\n");
        }
        return "Project " +
                "id=" + super.getId() +
                ", name='" + super.getName() + stringBuilder.toString();
    }
}
