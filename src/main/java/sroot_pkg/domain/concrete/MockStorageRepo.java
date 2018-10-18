package sroot_pkg.domain.concrete;

import sroot_pkg.domain.abstr.IStorageRepo;
import sroot_pkg.domain.model.Device;
import sroot_pkg.domain.model.ServerBox;

import java.util.List;

public class MockStorageRepo implements IStorageRepo {

    public List<ServerBox> getServerBoxes() {
        return null;
    }

    public List<Device> getDevices() {
        return null;
    }

}
