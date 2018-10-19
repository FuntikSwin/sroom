package sroom_pkg.ui.model;

import sroom_pkg.domain.model.DeviceSlot;

import java.util.List;

public class AddSlotInterfaceModel {

    private List<DeviceSlot> deviceSlots;
    private String name;
    private int selectedDeviceId;
    private int selectedDeviceSlotId;

    public AddSlotInterfaceModel() {
    }

    public List<DeviceSlot> getDeviceSlots() {
        return deviceSlots;
    }

    public void setDeviceSlots(List<DeviceSlot> deviceSlots) {
        this.deviceSlots = deviceSlots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelectedDeviceId() {
        return selectedDeviceId;
    }

    public void setSelectedDeviceId(int selectedDeviceId) {
        this.selectedDeviceId = selectedDeviceId;
    }

    public int getSelectedDeviceSlotId() {
        return selectedDeviceSlotId;
    }

    public void setSelectedDeviceSlotId(int selectedDeviceSlotId) {
        this.selectedDeviceSlotId = selectedDeviceSlotId;
    }
}
