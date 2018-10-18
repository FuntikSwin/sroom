package sroot_pkg.ui.controller;

import sroot_pkg.domain.abstr.IStorageRepo;
import sroot_pkg.domain.concrete.DbStorageRepo;
import sroot_pkg.domain.model.ComboBoxItem;
import sroot_pkg.domain.model.Device;
import sroot_pkg.domain.model.ServerBox;
import sroot_pkg.ui.model.MainFrameModel;
import sroot_pkg.ui.view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainFrameController {

    private MainFrameModel model = new MainFrameModel();

    private MainFrame mainFrame;
    private JComboBox cbServerBoxes;
    private JTable tblDevices;

    public MainFrameController() {
        IStorageRepo storageRepo = new DbStorageRepo();
        try {
            model.setServerBoxes(storageRepo.getServerBoxes());
            model.setDevices(storageRepo.getDevices());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initComponent();
        initListeners();
    }

    public void show() {
        mainFrame.setVisible(true);
    }

    private void initListeners() {
        //button1.addActionListener(new Button1Listener());
        cbServerBoxes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComboBoxItem item = (ComboBoxItem) cbServerBoxes.getSelectedItem();
                JOptionPane.showMessageDialog(null, item.getKey());
            }
        });
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
                tableModel.addRow(new Object[] {item.getServerBox().getName(), Integer.toString(item.getNum()), item.getName()});
            }
        }
    }
}
