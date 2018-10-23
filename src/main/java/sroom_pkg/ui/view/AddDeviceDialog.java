package sroom_pkg.ui.view;

import javax.swing.*;
import java.awt.event.*;

public class AddDeviceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfDeviceName;
    private JSpinner spDeviceSize;
    private JSpinner spDeviceNum;
    private JComboBox cbServerBoxes;
    private JButton addServerBoxButton;
    private JTextField tfDesc;
    private JCheckBox noNumCheckBox;

    public AddDeviceDialog() {
        setContentPane(contentPane);
        setModal(true);

        setTitle("Add new device");
        //spDeviceNum.setModel(new SpinnerNumberModel(1.0, 1.0, 200.0, 1.0));
        spDeviceNum.setModel(new SpinnerNumberModel(1, 1, 200, 1));
        //spDeviceSize.setModel(new SpinnerNumberModel(1.0, 1.0, 10.0, 1.0));
        spDeviceSize.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        getRootPane().setDefaultButton(buttonOK);

        /*buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });*/

        // call onCancel() when cross is clicked
        /*setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });*/

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /*private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }*/

    public JTextField getTfDeviceName() {
        return tfDeviceName;
    }

    public JSpinner getSpDeviceNum() {
        return spDeviceNum;
    }

    public JSpinner getSpDeviceSize() {
        return spDeviceSize;
    }

    public JComboBox getCbServerBoxes() {
        return cbServerBoxes;
    }

    public JButton getAddServerBoxButton() {
        return addServerBoxButton;
    }

    public JButton getButtonCancel() {
        return buttonCancel;
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    public JTextField getTfDesc() {
        return tfDesc;
    }

    public JCheckBox getNoNumCheckBox() {
        return noNumCheckBox;
    }

    public void setNoNumCheckBox(JCheckBox noNumCheckBox) {
        this.noNumCheckBox = noNumCheckBox;
    }
}
