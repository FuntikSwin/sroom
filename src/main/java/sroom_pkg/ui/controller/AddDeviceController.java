package sroom_pkg.ui.controller;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.concrete.DbStorageRepo;
import sroom_pkg.domain.model.ComboBoxItem;
import sroom_pkg.domain.model.ServerBox;
import sroom_pkg.ui.model.AddDeviceModel;
import sroom_pkg.ui.view.AddDeviceDialog;
import sroom_pkg.ui.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddDeviceController {

    private IStorageRepo storageRepo = new DbStorageRepo();

    private MainFrame parent;
    private AddDeviceModel model;
    private AddDeviceDialog dialog;

    private JTextField tfDeviceName;
    private JSpinner spDeviceNum;
    private JSpinner spDeviceSize;
    private JComboBox cbServerBoxes;
    private JButton addServerBoxButton;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JTextField tfDesc;

    public AddDeviceController(MainFrame parent, AddDeviceModel model) {
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
        dialog = new AddDeviceDialog();

        tfDeviceName = dialog.getTfDeviceName();
        tfDeviceName.setText(model.getName());

        spDeviceNum = dialog.getSpDeviceNum();
        spDeviceNum.setValue(model.getNum());

        spDeviceSize = dialog.getSpDeviceSize();
        spDeviceSize.setValue(model.getSize());

        cbServerBoxes = dialog.getCbServerBoxes();

        updateServerBoxes();

        addServerBoxButton = dialog.getAddServerBoxButton();
        buttonCancel = dialog.getButtonCancel();
        buttonOK = dialog.getButtonOK();

        tfDesc = dialog.getTfDesc();
        tfDesc.setText(model.getDesc());
    }

    private void initListener() {
        tfDeviceName.setText(model.getName());

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        addServerBoxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newServerBoxName = JOptionPane.showInputDialog("Название нового шкафа:");
                if (newServerBoxName != null && !newServerBoxName.equals("")) {
                    try {
                        storageRepo.addServerBox(newServerBoxName);
                        updateServerBoxes();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                try {
                    if (model.isAddDevice()) {
                        storageRepo.addDevice(
                                tfDeviceName.getText()
                                //, ((Double) spDeviceNum.getValue()).intValue()
                                , (int) spDeviceNum.getValue()
                                //, ((Double) spDeviceSize.getValue()).intValue()
                                , (int) spDeviceSize.getValue()
                                , getSelectedServerBoxId()
                                , tfDesc.getText());
                    } else {
                        storageRepo.updateDevice(model.getModifyDeviceId()
                                , tfDeviceName.getText()
                                , (int) spDeviceNum.getValue()
                                , (int) spDeviceSize.getValue()
                                , getSelectedServerBoxId()
                                , tfDesc.getText());
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private int getSelectedServerBoxId() {
        String serverBoxId = ((ComboBoxItem) cbServerBoxes.getSelectedItem()).getKey();
        return Integer.parseInt(serverBoxId);
    }

    private void updateServerBoxes() {
        cbServerBoxes.removeAllItems();
        try {
            model.setServerBoxes(storageRepo.getServerBoxes());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (model.getServerBoxes() != null) {
            for (ServerBox item : model.getServerBoxes()) {
                cbServerBoxes.addItem(item);
                if (model.getSelectedServerBoxId() == item.getId()) {
                    cbServerBoxes.setSelectedItem(item);
                }
            }
        }
    }

    public AddDeviceDialog getDialog() {
        return dialog;
    }
}
