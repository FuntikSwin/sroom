package sroom_pkg.domain.model;

import java.util.Objects;

public class DeviceSlot extends ComboBoxItem {

    private int id;
    private String name;
    private Integer deviceId;
    private Device device;

    public DeviceSlot(int id, String name, Integer deviceId) {
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

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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
        DeviceSlot that = (DeviceSlot) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
