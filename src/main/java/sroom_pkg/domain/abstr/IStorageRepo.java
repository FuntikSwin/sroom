package sroom_pkg.domain.abstr;

import sroom_pkg.domain.model.Device;
import sroom_pkg.domain.model.DeviceSlot;
import sroom_pkg.domain.model.ServerBox;
import sroom_pkg.domain.model.SlotInterface;

import java.sql.SQLException;
import java.util.List;

public interface IStorageRepo {

    List<ServerBox> getServerBoxes() throws SQLException;

    List<Device> getDevices(int serverBoxId) throws SQLException;
    void removeDevice(int deviceId) throws SQLException;

    List<SlotInterface> getSlotInterfaces(int deviceId) throws SQLException;
    void removeSlotInterface(int slotInterfaceId) throws SQLException;
    void addSlotInterface(int deviceSlotId, String name, Integer linkId) throws SQLException;

    List<DeviceSlot> getDeviceSlots(int deviceId) throws SQLException;
    void addDeviceSlot(String name, int deviceId) throws SQLException;
}
