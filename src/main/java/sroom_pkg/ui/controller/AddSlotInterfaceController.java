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
        addSlotButton = dialog.getAddSlotButton();
        renameSlotButton = dialog.getRenameSlotButton();
    }

    private void initListener() {
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setName(tfInterfaceName.getText());
                ComboBoxItem item = (ComboBoxItem) cbDeviceSlots.getSelectedItem();
                model.setSelectedDeviceSlotId(Integer.parseInt(item.getKey()));
                dialog.dispose();
                try {
                    storageRepo.addSlotInterface(model.getSelectedDeviceSlotId(), model.getName(), null);
                } catch (SQLException e1) {
                    e1.printStackTrace();
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

        for (DeviceSlot item: deviceSlots) {
            cbDeviceSlots.addItem(item);
        }
    }

    public AddSlotInterfaceDialog getDialog() {
        return dialog;
    }
}
