package sroom_pkg.domain.abstr;

import sroom_pkg.domain.model.*;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface IStorageRepo {

    String getDbFileName();
    void setDb(File fileDb);

    List<ServerBox> getServerBoxes() throws SQLException;
    void addServerBox(String name) throws SQLException;

    List<Device> getDevices(int serverBoxId, int deviceId) throws SQLException;
    void removeDevice(int deviceId) throws SQLException;
    void addDevice(String name, Integer num, int size, int serverBoxId, String desc) throws SQLException;
    void updateDevice(int deviceId, String name, Integer num, int size, int serverBoxId, String desc) throws SQLException;

    List<SlotInterface> getSlotInterfaces(int deviceId, int slotInterfaceId) throws SQLException;
    void removeSlotInterface(int slotInterfaceId) throws SQLException;
    void addSlotInterface(int deviceSlotId, String name, String desc, Integer linkId) throws SQLException;
    void updateSlotInterface(int slotInterfaceId, int deiceSlotId, String name, String desc, Integer linkId) throws SQLException;

    List<DeviceSlot> getDeviceSlots(int deviceId) throws SQLException;
    void addDeviceSlot(String name, int deviceId) throws SQLException;
    void updateDeviceSlot(int deviceSlotId, String name) throws SQLException;

    List<InterfaceType> getInterfaceTypes() throws SQLException;

    SlotInterface getSlotInterfaceByLink(int linkId, int sourceSlotInterfaceId) throws SQLException;
    List<SlotInterface> getFillLinkedSlotInterfaces(List<SlotInterface> sources) throws SQLException;
    void addLink(int sourceSlotInterfaceId, int targetInterfaceId, int interfaceTypeId) throws SQLException;
    void updateLink(int linkId, int interfaceTypeId, int sourceSlotInterfaceId, int targetSlotInterfaceId) throws SQLException;
}
