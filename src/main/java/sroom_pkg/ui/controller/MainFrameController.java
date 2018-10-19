package sroom_pkg.ui.controller;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.concrete.DbStorageRepo;
import sroom_pkg.domain.model.ComboBoxItem;
import sroom_pkg.domain.model.Device;
import sroom_pkg.domain.model.ServerBox;
import sroom_pkg.domain.model.SlotInterface;
import sroom_pkg.ui.model.MainFrameModel;
import sroom_pkg.ui.view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class MainFrameController {

    private final IStorageRepo storageRepo = new DbStorageRepo();
    private MainFrameModel model = new MainFrameModel();

    private MainFrame mainFrame;
    private JComboBox cbServerBoxes;
    private JTable tblDevices;
    private JTable tblInterfaces;
    private JButton removeInterfaceButton;

    public MainFrameController() {
        try {
            model.setServerBoxes(storageRepo.getServerBoxes());
            model.setDevices(storageRepo.getDevices(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initComponent();
        initListeners();
    }

    public void show() {
        mainFrame.setVisible(true);
    }

    private void initComponent() {
        mainFrame = new MainFrame();

        cbServerBoxes = mainFrame.getCbServerBoxes();
        cbServerBoxes.addItem(new ServerBox(0, "Все"));
        if (model != null && model.getServerBoxes() != null) {
            for (ServerBox item: model.getServerBoxes()) {
                cbServerBoxes.addItem(item);
            }
        }

        tblDevices = mainFrame.getTblDevices();
        if (model != null && model.getDevices() != null) {
            DefaultTableModel tableModel = (DefaultTableModel) tblDevices.getModel();
            for (Device item: model.getDevices()) {
                tableModel.addRow(new Object[] {item.getId(), item.getServerBox().getName(), Integer.toString(item.getNum()), item.getName()});
            }
        }

        tblInterfaces = mainFrame.getTblInterfaces();

        removeInterfaceButton = mainFrame.getRemoveInterfaceButton();
    }

    private void initListeners() {
        cbServerBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComboBoxItem cbItem = (ComboBoxItem) cbServerBoxes.getSelectedItem();
                //JOptionPane.showMessageDialog(null, item.getKey() + "; " + item.getValue());

                DefaultTableModel tableModel = (DefaultTableModel) tblInterfaces.getModel();
                for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                    tableModel.removeRow(i);
                }
                tableModel = (DefaultTableModel) tblDevices.getModel();
                for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                    tableModel.removeRow(i);
                }

                try {
                    List<Device> data = storageRepo.getDevices(Integer.parseInt(cbItem.getKey()));
                    for (Device item: data) {
                        tableModel.addRow(new Object[] {item.getId(), item.getServerBox().getName(), Integer.toString(item.getNum()), item.getName()});
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        tblDevices.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel tableModel = (DefaultTableModel) tblInterfaces.getModel();
                for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                    tableModel.removeRow(i);
                }

                int i = tblDevices.getSelectedRow();
                int deviceId = (Integer) tblDevices.getModel().getValueAt(i, 0);
                try {
                    List<SlotInterface> data = storageRepo.getSlotInterfaces(deviceId);
                    for (SlotInterface item: data) {
                        tableModel.addRow(new Object[] {item.getId(), item.getDeviceSlot().getName(), item.getName()});
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                super.mouseClicked(e);
            }
        });

        removeInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tblInterfaces.getSelectedRow();
                int interfaceId = (int) tblInterfaces.getModel().getValueAt(i, 0);
                JOptionPane.showMessageDialog(null, "InterfaceId: " + Integer.toString(interfaceId));
            }
        });
    }

}
