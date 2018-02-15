package model;

import java.util.HashSet;
import java.util.Set;

public class Customer extends Core {
    private Set<Project> projects;

    public Customer(String firstName) {
        super(firstName);
        projects = new HashSet<>();
    }

    public Customer(Integer id, String firstName) {
        super(id, firstName);
        projects = new HashSet<>();
    }

    public String getFirstName() {
        return super.getName();
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public String getProjectsString(){
        if (projects.isEmpty()) return "0";
        StringBuilder result = new StringBuilder();
        for (Project project : projects){
            result.append(project.getId() + ",");
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }

    public void setProject(Project project) {
        this.projects.add(project);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", \nprojects=\n");
        for (Project project : projects){
            stringBuilder.append("\t" + project.toString() + "\n");
        }
        return "Customer " +
                "id=" + super.getId() +
                ", firstName='" + getFirstName() + '\'' +
                stringBuilder.toString();
    }

}
