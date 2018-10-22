package sroom_pkg.ui.model;

import sroom_pkg.domain.model.ServerBox;

import java.util.List;

public class AddDeviceModel {

    private String name;
    private int num;
    private int size;
    private List<ServerBox> serverBoxes;
    private int selectedServerBoxId;

    public AddDeviceModel() {
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

    public List<ServerBox> getServerBoxes() {
        return serverBoxes;
    }

    public void setServerBoxes(List<ServerBox> serverBoxes) {
        this.serverBoxes = serverBoxes;
    }

    public int getSelectedServerBoxId() {
        return selectedServerBoxId;
    }

    public void setSelectedServerBoxId(int selectedServerBoxId) {
        this.selectedServerBoxId = selectedServerBoxId;
    }
}
