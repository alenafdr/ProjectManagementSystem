package model;

public class Core {
    private Integer id;
    private String name;

    public Core(String name) {
        this.name = name;
    }

    public Core(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Core core = (Core) o;

        return (getId().equals(core.getId()));

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getId().hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
