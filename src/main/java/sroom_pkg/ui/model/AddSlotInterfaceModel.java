package sroom_pkg.ui.model;

import sroom_pkg.domain.model.DeviceSlot;

import java.util.List;

public class AddSlotInterfaceModel {

    private List<DeviceSlot> deviceSlots;
    private String name;
    private String Desc;
    private int selectedDeviceId;
    private int selectedDeviceSlotId;
    private boolean addInterface;
    private int modifyInterfaceId;
    private int interfaceLinkId;

    public AddSlotInterfaceModel() {
        addInterface = true;
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

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
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

    public boolean isAddInterface() {
        return addInterface;
    }

    public void setAddInterface(boolean addInterface) {
        this.addInterface = addInterface;
    }

    public int getModifyInterfaceId() {
        return modifyInterfaceId;
    }

    public void setModifyInterfaceId(int modifyInterfaceId) {
        this.modifyInterfaceId = modifyInterfaceId;
    }

    public int getInterfaceLinkId() {
        return interfaceLinkId;
    }

    public void setInterfaceLinkId(int interfaceLinkId) {
        this.interfaceLinkId = interfaceLinkId;
    }
}
