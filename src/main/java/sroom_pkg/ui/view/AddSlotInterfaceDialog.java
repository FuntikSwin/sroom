package sroom_pkg.ui.view;

import javax.swing.*;
import java.awt.event.*;

public class AddSlotInterfaceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfInterfaceName;
    private JComboBox cbDeviceSlots;
    private JButton addSlotButton;

    public AddSlotInterfaceDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Add new interface");

        /*buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });*/

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /*private void onOK() {
        // add your code here
        dispose();
    }*/

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public JComboBox getCbDeviceSlots() {
        return cbDeviceSlots;
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    public JTextField getTfInterfaceName() {
        return tfInterfaceName;
    }

    public JButton getAddSlotButton() {
        return addSlotButton;
    }
}
