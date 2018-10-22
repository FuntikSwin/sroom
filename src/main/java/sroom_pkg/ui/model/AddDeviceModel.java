package sroom_pkg.ui.model;

import sroom_pkg.domain.model.ServerBox;

import java.util.List;

public class AddDeviceModel {

    private String name;
    private String desc;
    private int num;
    private int size;
    private List<ServerBox> serverBoxes;
    private int selectedServerBoxId;
    private boolean addDevice;
    private int modifyDeviceId;

    public AddDeviceModel() {
        addDevice = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public boolean isAddDevice() {
        return addDevice;
    }

    public void setAddDevice(boolean addDevice) {
        this.addDevice = addDevice;
    }

    public int getModifyDeviceId() {
        return modifyDeviceId;
    }

    public void setModifyDeviceId(int modifyDeviceId) {
        this.modifyDeviceId = modifyDeviceId;
    }
}
