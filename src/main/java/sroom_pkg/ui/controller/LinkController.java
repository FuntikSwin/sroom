package sroom_pkg.ui.controller;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.concrete.DbStorageRepo;
import sroom_pkg.domain.model.*;
import sroom_pkg.ui.model.LinkModel;
import sroom_pkg.ui.view.LinkDialog;
import sroom_pkg.ui.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LinkController {

    private final IStorageRepo storageRepo = new DbStorageRepo();

    private MainFrame parent;
    private LinkDialog dialog;
    private LinkModel model;

    private JTextArea taInterfaceInfo;
    private JComboBox cbServerBoxes;
    private JComboBox cbDevices;
    private JComboBox cbDeviceSlots;
    private JComboBox cbSlotInterfaces;
    private JComboBox cbInterfaceType;

    public LinkController(MainFrame parent, LinkModel model) {
        this.parent = parent;
        this.model = model;
        initComponent();
        initListener();
    }

    public void show() {
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private void initComponent() {
        dialog = new LinkDialog();
        taInterfaceInfo = dialog.getTaInterfaceInfo();
        if (model.getSlotInterface() != null) {
            taInterfaceInfo.append("ServerBox: " + model.getSlotInterface().getDeviceSlot().getDevice().getServerBox().getName() + "\n");
            taInterfaceInfo.append("Device: " + model.getSlotInterface().getDeviceSlot().getDevice().getName() + "\n");
            taInterfaceInfo.append("DeviceNum: " + model.getSlotInterface().getDeviceSlot().getDevice().getNum() + "\n");
            taInterfaceInfo.append("Slot: " + model.getSlotInterface().getDeviceSlot().getName() + "\n");
            taInterfaceInfo.append("Interface: " + model.getSlotInterface().getName());
        }

        cbServerBoxes = dialog.getCbServerBoxes();
        cbDevices = dialog.getCbDevices();
        cbDeviceSlots = dialog.getCbDeviceSlots();
        cbSlotInterfaces = dialog.getCbSlotInterfaces();
        updateServerBoxes();

        cbInterfaceType = dialog.getCbInterfaceType();
        cbInterfaceType.removeAllItems();
        List<InterfaceType> interfaceTypes = new ArrayList<>();
        try {
            interfaceTypes = storageRepo.getInterfaceTypes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (InterfaceType item: interfaceTypes) {
            cbInterfaceType.addItem(item);
        }
    }

    private void updateServerBoxes() {
        cbServerBoxes.removeAllItems();
        List<ServerBox> serverBoxes = new ArrayList<>();
        try {
            serverBoxes = storageRepo.getServerBoxes();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        for (ServerBox item: serverBoxes) {
            cbServerBoxes.addItem(item);
        }
        updateDevices();
    }

    private void updateDevices() {
        cbDevices.removeAllItems();
        List<Device> devices = new ArrayList<>();
        try {
            Integer serverBoxId = getSelectedServerBoxId();
            if (serverBoxId == null) {
                return;
            }
            devices = storageRepo.getDevices(serverBoxId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        for (Device item: devices) {
            cbDevices.addItem(item);
        }
        updateDeviceSlots();
    }

    private void updateDeviceSlots() {
        cbDeviceSlots.removeAllItems();
        List<DeviceSlot> deviceSlots = new ArrayList<>();
        try {
            Integer deviceId = getSelectedDeviceId();
            if (deviceId == null) {
                return;
            }
            deviceSlots = storageRepo.getDeviceSlots(deviceId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        for (DeviceSlot item: deviceSlots) {
            cbDeviceSlots.addItem(item);
        }
        updateSlotInterfaces();
    }

    private void updateSlotInterfaces() {
        cbSlotInterfaces.removeAllItems();
        List<SlotInterface> slotInterfaces = new ArrayList<>();
        try {
            Integer deviceId = getSelectedDeviceId();
            if (deviceId == null) {
                return;
            }
            slotInterfaces = storageRepo.getSlotInterfaces(deviceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (SlotInterface item: slotInterfaces) {
            if (item.getSlotId() != getSelectedDeviceSlotId()) {
                continue;
            }
            cbSlotInterfaces.addItem(item);
        }
    }

    private Integer getSelectedServerBoxId() {
        if (cbServerBoxes.getSelectedItem() == null) {
            return null;
        }
        String keyStr = ((ComboBoxItem) cbServerBoxes.getSelectedItem()).getKey();
        return Integer.parseInt(keyStr);
    }

    private Integer getSelectedDeviceId() {
        if (cbDevices.getSelectedItem() == null) {
            return null;
        }
        String keyStr = ((ComboBoxItem) cbDevices.getSelectedItem()).getKey();
        return Integer.parseInt(keyStr);
    }

    private Integer getSelectedDeviceSlotId() {
        if (cbDeviceSlots.getSelectedItem() == null) {
            return null;
        }
        String keyStr = ((ComboBoxItem) cbDeviceSlots.getSelectedItem()).getKey();
        return Integer.parseInt(keyStr);
    }

    private void initListener() {
        cbServerBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDevices();
            }
        });

        cbDevices.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDeviceSlots();
            }
        });

        cbDeviceSlots.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSlotInterfaces();
            }
        });
    }

    public LinkDialog getDialog() {
        return dialog;
    }
}
