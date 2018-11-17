package sroom_pkg.domain.model;

public class SlotInterface extends ComboBoxItem {

    private int id;
    private Integer slotId;
    private String name;
    private Integer linkId;
    private String desc;

    private DeviceSlot deviceSlot;
    private InterfaceType interfaceType;
    private SlotInterface linkSlotInterface;

    public SlotInterface(int id, Integer slotId, String name, Integer linkId, String desc) {
        this.id = id;
        this.slotId = slotId;
        this.name = name;
        this.linkId = linkId;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public DeviceSlot getDeviceSlot() {
        return deviceSlot;
    }

    public void setDeviceSlot(DeviceSlot deviceSlot) {
        this.deviceSlot = deviceSlot;
    }

    public InterfaceType getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(InterfaceType interfaceType) {
        this.interfaceType = interfaceType;
    }

    public SlotInterface getLinkSlotInterface() {
        return linkSlotInterface;
    }

    public void setLinkSlotInterface(SlotInterface linkSlotInterface) {
        this.linkSlotInterface = linkSlotInterface;
    }

    public String getLinkSlotInterfacePath() {
        if (linkSlotInterface == null) {
            return "";
        }

        return "[" + interfaceType.getName() + "] "
                + linkSlotInterface.getDeviceSlot().getDevice().getServerBox().getName() + ". "
                + "[" + linkSlotInterface.getDeviceSlot().getDevice().getNum() + "] "
                + linkSlotInterface.getDeviceSlot().getDevice().getName() + " "
                + "(" + linkSlotInterface.getDeviceSlot().getDevice().getDesc() + ") "
                + "Слот: " + linkSlotInterface.getDeviceSlot().getName() + ", "
                + "Интерфейс: " + linkSlotInterface.getName();
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
}
