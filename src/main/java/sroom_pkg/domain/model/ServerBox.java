package sroom_pkg.domain.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerBox serverBox = (ServerBox) o;
        return id == serverBox.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
