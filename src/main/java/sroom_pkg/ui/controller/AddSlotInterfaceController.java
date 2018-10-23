package sroom_pkg.ui.controller;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.concrete.DbStorageRepo;
import sroom_pkg.domain.model.ComboBoxItem;
import sroom_pkg.domain.model.DeviceSlot;
import sroom_pkg.ui.model.AddSlotInterfaceModel;
import sroom_pkg.ui.view.AddSlotInterfaceDialog;
import sroom_pkg.ui.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddSlotInterfaceController {

    private IStorageRepo storageRepo = new DbStorageRepo();

    private AddSlotInterfaceDialog dialog;
    private MainFrame parent;
    private AddSlotInterfaceModel model;
    private JComboBox cbDeviceSlots;
    private JButton buttonOK;
    private JTextField tfInterfaceName;
    private JButton addSlotButton;
    private JButton renameSlotButton;
    private JTextField tfDesc;

    public AddSlotInterfaceController(MainFrame parent, AddSlotInterfaceModel model) {
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
        dialog = new AddSlotInterfaceDialog();
        cbDeviceSlots = dialog.getCbDeviceSlots();

        /*for(DeviceSlot item: model.getDeviceSlots()) {
            cbDeviceSlots.addItem(item);
        }*/
        updateDeviceSlots();

        buttonOK = dialog.getButtonOK();
        tfInterfaceName = dialog.getTfInterfaceName();
        tfInterfaceName.setText(model.getName());
        addSlotButton = dialog.getAddSlotButton();
        renameSlotButton = dialog.getRenameSlotButton();
        tfDesc = dialog.getTfDesc();
        tfDesc.setText(model.getDesc());
    }

    private void initListener() {
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                model.setName(tfInterfaceName.getText());
                ComboBoxItem item = (ComboBoxItem) cbDeviceSlots.getSelectedItem();
                model.setSelectedDeviceSlotId(Integer.parseInt(item.getKey()));
                model.setDesc(tfDesc.getText());
                try {
                    if (model.isAddInterface()) {
                        storageRepo.addSlotInterface(model.getSelectedDeviceSlotId(), model.getName(), model.getDesc(), null);
                    } else {
                        storageRepo.updateSlotInterface(model.getModifyInterfaceId(), model.getSelectedDeviceSlotId(), model.getName(), model.getDesc(), model.getInterfaceLinkId());
                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Внимание!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addSlotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tmp = JOptionPane.showInputDialog("Название нового слота:");
                if (tmp != null && !tmp.equals("")) {
                    try {
                        storageRepo.addDeviceSlot(tmp, model.getSelectedDeviceId());
                        updateDeviceSlots();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        renameSlotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog("Новое имя слота:");
                if (newName != null && !newName.equals("")) {
                    ComboBoxItem item = (ComboBoxItem) cbDeviceSlots.getSelectedItem();
                    try {
                        storageRepo.updateDeviceSlot(Integer.parseInt(item.getKey()), newName);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    updateDeviceSlots();
                }
            }
        });
    }

    private void updateDeviceSlots() {
        cbDeviceSlots.removeAllItems();
        List<DeviceSlot> deviceSlots = new ArrayList<>();
        try {
            deviceSlots = storageRepo.getDeviceSlots(model.getSelectedDeviceId());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        DeviceSlot currSlot = null;
        for (DeviceSlot item : deviceSlots) {
            currSlot = item;
            cbDeviceSlots.addItem(item);
            if (model.getSelectedDeviceSlotId() > 0 && item.getId() == model.getSelectedDeviceSlotId()) {
                cbDeviceSlots.setSelectedItem(item);
            }
        }
        if (model.isAddInterface() && currSlot != null) {
            cbDeviceSlots.setSelectedItem(currSlot);
        }
    }

    public AddSlotInterfaceDialog getDialog() {
        return dialog;
    }
}
