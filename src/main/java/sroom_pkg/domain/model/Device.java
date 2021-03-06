package sroom_pkg.domain.model;

import java.util.Objects;

public class Device extends ComboBoxItem {

    private int id;
    private String name;
    private Integer num;
    private int size;
    private Integer serverBoxId;
    private String desc;
    private ServerBox serverBox;

    public Device(int id, String name, Integer num, int size, Integer serverBoxId, String desc) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.size = size;
        this.serverBoxId = serverBoxId;
        this.desc = desc;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getServerBoxId() {
        return serverBoxId;
    }

    public void setServerBoxId(Integer serverBoxId) {
        this.serverBoxId = serverBoxId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ServerBox getServerBox() {
        return serverBox;
    }

    public void setServerBox(ServerBox serverBox) {
        this.serverBox = serverBox;
    }

    @Override
    public String getKey() {
        return Integer.toString(id);
    }

    @Override
    public String getValue() {
        return Integer.toString(num) + ": " + name;
    }

    @Override
    public String toString() {
        return Integer.toString(num) + ": " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return id == device.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
