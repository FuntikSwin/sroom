package sroom_pkg.ui.model;

import sroom_pkg.domain.model.Device;
import sroom_pkg.domain.model.ServerBox;

import java.util.List;

public class MainFrameModel {

    private List<ServerBox> serverBoxes;
    private List<Device> devices;

    public MainFrameModel() {
    }

    public List<ServerBox> getServerBoxes() {
        return serverBoxes;
    }

    public void setServerBoxes(List<ServerBox> serverBoxes) {
        this.serverBoxes = serverBoxes;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
