package sroom_pkg.ui.controller;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.concrete.DbStorageRepo;
import sroom_pkg.domain.model.ComboBoxItem;
import sroom_pkg.domain.model.Device;
import sroom_pkg.domain.model.ServerBox;
import sroom_pkg.domain.model.SlotInterface;
import sroom_pkg.ui.model.AddSlotInterfaceModel;
import sroom_pkg.ui.view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainFrameController {

    private final IStorageRepo storageRepo = new DbStorageRepo();

    private MainFrame mainFrame;
    private JComboBox cbServerBoxes;
    private JTable tblDevices;
    private JTable tblInterfaces;
    private JButton removeInterfaceButton;
    private JButton addInterfaceButton;

    public MainFrameController() {
        initComponent();
        initListeners();
    }

    public void show() {
        mainFrame.pack();
        mainFrame.setSize(500, 500);
        //mainFrame.setLocationByPlatform(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void initComponent() {
        mainFrame = new MainFrame();

        cbServerBoxes = mainFrame.getCbServerBoxes();
        cbServerBoxes.addItem(new ServerBox(0, "Все"));
        try {
            List<ServerBox> serverBoxes = storageRepo.getServerBoxes();
            for (ServerBox item: serverBoxes) {
                cbServerBoxes.addItem(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblDevices = mainFrame.getTblDevices();
        tblInterfaces = mainFrame.getTblInterfaces();
        updateDevicesTable();

        removeInterfaceButton = mainFrame.getRemoveInterfaceButton();
        addInterfaceButton = mainFrame.getAddInterfaceButton();
    }

    private void initListeners() {
        cbServerBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDevicesTable();
            }
        });

        tblDevices.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateSlotInterfacesTable();
                super.mouseClicked(e);
            }
        });

        removeInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblInterfaces.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Не выбран интерфейс!");
                    return;
                }
                int interfaceId = (int) tblInterfaces.getModel().getValueAt(selectedRow, 0);
                //JOptionPane.showMessageDialog(null, "InterfaceId: " + Integer.toString(interfaceId));

                try {
                    storageRepo.removeSlotInterface(interfaceId);
                    updateSlotInterfacesTable();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        addInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSlotInterfaceModel dlgModel = new AddSlotInterfaceModel();
                try {
                    int deviceId = getSelectedDeviceId();
                    dlgModel.setDeviceSlots(storageRepo.getDeviceSlots(deviceId));
                    dlgModel.setName("");
                    dlgModel.setSelectedDeviceId(deviceId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return;
                }
                AddSlotInterfaceController dlg = new AddSlotInterfaceController(mainFrame, dlgModel);
                dlg.getDialog().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        updateSlotInterfacesTable();
                    }
                });
                dlg.show();
            }
        });
    }

    private int getSelectedServerBoxId() {
        String serverBoxId = ((ComboBoxItem) cbServerBoxes.getSelectedItem()).getKey();
        return Integer.parseInt(serverBoxId);
    }

    private int getSelectedDeviceId() throws Exception {
        int row = tblDevices.getSelectedRow();
        if (row == -1) {
            throw new Exception("Не выбрано устройство");
        }

        return (int) tblDevices.getModel().getValueAt(row, 0);
    }

    private void updateDevicesTable() {
        DefaultTableModel tableModel = (DefaultTableModel) tblDevices.getModel();
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        List<Device> data = new ArrayList<>();
        try {
            data = storageRepo.getDevices(getSelectedServerBoxId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Device item: data) {
            tableModel.addRow(new Object[] {item.getId(), item.getServerBox().getName(), Integer.toString(item.getNum()), item.getName()});
        }

        tableModel = (DefaultTableModel) tblInterfaces.getModel();
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    private void updateSlotInterfacesTable() {
        DefaultTableModel tableModel = (DefaultTableModel) tblInterfaces.getModel();
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        List<SlotInterface> data = new ArrayList<>();
        try {
            data = storageRepo.getSlotInterfaces(getSelectedDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (SlotInterface item: data) {
            tableModel.addRow(new Object[] {item.getId(), item.getDeviceSlot().getName(), item.getName()});
        }
    }

}
