package sroom_pkg.domain.abstr;

import sroom_pkg.domain.model.SlotInterface;

import java.io.IOException;
import java.util.List;

public interface IReportRepo {

    void generalReport(List<SlotInterface> slotInterfaces) throws IOException;

}
