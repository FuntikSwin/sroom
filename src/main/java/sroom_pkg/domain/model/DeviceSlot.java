package sroom_pkg.domain.model;

public class DeviceSlot {

    private int id;
    private String name;
    private int deviceId;
    private Device device;

    public DeviceSlot(int id, String name, int deviceId) {
        this.id = id;
        this.name = name;
        this.deviceId = deviceId;
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

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
