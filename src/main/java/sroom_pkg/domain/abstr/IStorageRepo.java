package sroom_pkg.domain.abstr;

import sroom_pkg.domain.model.*;

import java.sql.SQLException;
import java.util.List;

public interface IStorageRepo {

    List<ServerBox> getServerBoxes() throws SQLException;
    void addServerBox(String name) throws SQLException;

    List<Device> getDevices(int serverBoxId) throws SQLException;
    void removeDevice(int deviceId) throws SQLException;
    void addDevice(String name, int num, int size, int serverBoxId, String desc) throws SQLException;

    List<SlotInterface> getSlotInterfaces(int deviceId) throws SQLException;
    void removeSlotInterface(int slotInterfaceId) throws SQLException;
    void addSlotInterface(int deviceSlotId, String name, Integer linkId) throws SQLException;

    List<DeviceSlot> getDeviceSlots(int deviceId) throws SQLException;
    void addDeviceSlot(String name, int deviceId) throws SQLException;
    void updateDeviceSlot(int deviceSlotId, String name) throws SQLException;

    List<InterfaceType> getInterfaceTypes() throws SQLException;

    void addLink(int sourceSlotInterfaceId, int targetInterfaceId, int interfaceTypeId) throws SQLException;
}
