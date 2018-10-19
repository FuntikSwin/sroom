package sroom_pkg.domain.model;

public class SlotInterface {

    private int id;
    private Integer slotId;
    private String name;
    private Integer linkId;

    private DeviceSlot deviceSlot;

    public SlotInterface(int id, Integer slotId, String name, Integer linkId) {
        this.id = id;
        this.slotId = slotId;
        this.name = name;
        this.linkId = linkId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public DeviceSlot getDeviceSlot() {
        return deviceSlot;
    }

    public void setDeviceSlot(DeviceSlot deviceSlot) {
        this.deviceSlot = deviceSlot;
    }
}
