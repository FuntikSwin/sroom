package sroot_pkg.domain.abstr;

import sroot_pkg.domain.model.Device;
import sroot_pkg.domain.model.ServerBox;

import java.sql.SQLException;
import java.util.List;

public interface IStorageRepo {

    List<ServerBox> getServerBoxes() throws SQLException;
    List<Device> getDevices();
}
