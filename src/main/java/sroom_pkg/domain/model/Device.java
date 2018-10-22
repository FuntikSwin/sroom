package sroom_pkg.domain.model;

public class Device {

    private int id;
    private String name;
    private int num;
    private int size;
    private Integer serverBoxId;
    private String desc;
    private ServerBox serverBox;

    public Device(int id, String name, int num, int size, Integer serverBoxId, String desc) {
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
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
}
