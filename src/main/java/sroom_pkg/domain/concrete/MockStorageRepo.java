package sroom_pkg.domain.concrete;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.model.Device;
import sroom_pkg.domain.model.ServerBox;

import java.util.List;

public class MockStorageRepo implements IStorageRepo {

    public List<ServerBox> getServerBoxes() {
        return null;
    }

    public List<Device> getDevices() {
        return null;
    }

}
