package sroom_pkg.ui.view;

import javax.swing.*;
import java.awt.event.*;

public class LinkDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea taInterfaceInfo;
    private JComboBox cbServerBoxes;
    private JComboBox cbDevices;
    private JComboBox cbDeviceSlots;
    private JComboBox cbSlotInterfaces;
    private JComboBox cbInterfaceType;

    public LinkDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Связь интерфейса");

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public JTextArea getTaInterfaceInfo() {
        return taInterfaceInfo;
    }

    public JComboBox getCbServerBoxes() {
        return cbServerBoxes;
    }

    public JComboBox getCbDevices() {
        return cbDevices;
    }

    public JComboBox getCbDeviceSlots() {
        return cbDeviceSlots;
    }

    public JComboBox getCbSlotInterfaces() {
        return cbSlotInterfaces;
    }

    public JComboBox getCbInterfaceType() {
        return cbInterfaceType;
    }

    public JButton getButtonOK() {
        return buttonOK;
    }
}
