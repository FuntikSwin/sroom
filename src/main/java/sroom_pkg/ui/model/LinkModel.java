package sroom_pkg.ui.model;

import sroom_pkg.domain.model.SlotInterface;

public class LinkModel {

    private SlotInterface slotInterface;
    private SlotInterface targetSlotInterface;

    public LinkModel() {
    }

    public SlotInterface getSlotInterface() {
        return slotInterface;
    }

    public void setSlotInterface(SlotInterface slotInterface) {
        this.slotInterface = slotInterface;
    }

    public SlotInterface getTargetSlotInterface() {
        return targetSlotInterface;
    }

    public void setTargetSlotInterface(SlotInterface targetSlotInterface) {
        this.targetSlotInterface = targetSlotInterface;
    }
}
