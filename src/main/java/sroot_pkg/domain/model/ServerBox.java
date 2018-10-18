package sroot_pkg.domain.model;

public class ServerBox extends ComboBoxItem {

    private int id;
    private String name;

    public ServerBox(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return Integer.toString(id);
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
