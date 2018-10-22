package sroom_pkg.ui.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    private JButton addDeviceButton;
    private JPanel mainPanel;
    private JTable tblDevices;
    private JTable tblInterfaces;
    private JButton addInterfaceButton;
    private JComboBox cbServerBoxes;
    private JButton removeDeviceButton;
    private JButton removeInterfaceButton;
    private JButton linkButton;

    public MainFrame() {
        //pack();
        setContentPane(mainPanel);
        setTitle("Server room");

        //setSize(500, 500);

        DefaultTableModel tblModel = new DefaultTableModel();
        tblModel.addColumn("DeviceId");
        tblModel.addColumn("ServerBox");
        tblModel.addColumn("Num");
        tblModel.addColumn("Name");
        tblModel.addColumn("Desc");
        tblDevices.setModel(tblModel);
        tblDevices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblModel = new DefaultTableModel();
        tblModel.addColumn("InterfaceId");
        tblModel.addColumn("Slot");
        tblModel.addColumn("Name");
        tblInterfaces.setModel(tblModel);
        tblInterfaces.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public JComboBox getCbServerBoxes() {
        return cbServerBoxes;
    }

    public JTable getTblDevices() {
        return tblDevices;
    }

    public JTable getTblInterfaces() {
        return tblInterfaces;
    }

    public JButton getRemoveInterfaceButton() {
        return removeInterfaceButton;
    }

    public JButton getAddInterfaceButton() {
        return addInterfaceButton;
    }

    public JButton getRemoveDeviceButton() {
        return removeDeviceButton;
    }

    public JButton getAddDeviceButton() {
        return addDeviceButton;
    }
}
