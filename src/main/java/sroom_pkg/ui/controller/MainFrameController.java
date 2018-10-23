package sroom_pkg.ui.controller;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.concrete.DbStorageRepo;
import sroom_pkg.domain.model.*;
import sroom_pkg.ui.model.AddDeviceModel;
import sroom_pkg.ui.model.AddSlotInterfaceModel;
import sroom_pkg.ui.model.LinkModel;
import sroom_pkg.ui.view.LinkDialog;
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
    private JButton removeDeviceButton;
    private JButton addDeviceButton;
    private JButton linkButton;

    public MainFrameController() {
        initComponent();
        initListeners();
    }

    public void show() {
        mainFrame.pack();
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void initComponent() {
        mainFrame = new MainFrame();

        cbServerBoxes = mainFrame.getCbServerBoxes();
        cbServerBoxes.addItem(new ServerBox(0, "Все"));
        try {
            List<ServerBox> serverBoxes = storageRepo.getServerBoxes();
            for (ServerBox item : serverBoxes) {
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
        removeDeviceButton = mainFrame.getRemoveDeviceButton();
        addDeviceButton = mainFrame.getAddDeviceButton();
        linkButton = mainFrame.getLinkButton();
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
                if (e.getClickCount() == 1) {
                    updateSlotInterfacesTable();
                } else {
                    AddDeviceModel dlgModel = new AddDeviceModel();
                    dlgModel.setAddDevice(false);
                    List<Device> devices = new ArrayList<>();
                    try {
                        devices = storageRepo.getDevices(0, getSelectedDeviceId());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Внимание!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (devices.size() != 1) {
                        JOptionPane.showMessageDialog(null, "Не определено устройство!", "Внимание!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Device currDevice = devices.get(0);

                    dlgModel.setModifyDeviceId(currDevice.getId());
                    dlgModel.setName(currDevice.getName());
                    dlgModel.setDesc(currDevice.getDesc());
                    dlgModel.setNum(currDevice.getNum());
                    dlgModel.setSize(currDevice.getSize());
                    dlgModel.setServerBoxes(new ArrayList<ServerBox>());
                    dlgModel.setSelectedServerBoxId(currDevice.getServerBoxId());

                    AddDeviceController dlgController = new AddDeviceController(mainFrame, dlgModel);
                    dlgController.getDialog().addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            updateDevicesTable();
                        }
                    });
                    dlgController.show();
                }
                super.mouseClicked(e);
            }
        });

        tblInterfaces.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    AddSlotInterfaceModel dlgModel = new AddSlotInterfaceModel();
                    dlgModel.setAddInterface(false);
                    List<SlotInterface> slotInterfaces = new ArrayList<>();
                    try {
                        slotInterfaces = storageRepo.getSlotInterfaces(0, getSelectedSlotInterfaceId());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Внимание!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (slotInterfaces.size() != 1) {
                        JOptionPane.showMessageDialog(null, "Не определен интерфейс!", "Внимание!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    SlotInterface slotInterface = slotInterfaces.get(0);

                    dlgModel.setModifyInterfaceId(slotInterface.getId());
                    dlgModel.setInterfaceLinkId(slotInterface.getLinkId());
                    dlgModel.setName(slotInterface.getName());
                    dlgModel.setDesc(slotInterface.getDesc());
                    dlgModel.setSelectedDeviceSlotId(slotInterface.getSlotId());
                    dlgModel.setSelectedDeviceId(slotInterface.getDeviceSlot().getDeviceId());

                    AddSlotInterfaceController dlgController = new AddSlotInterfaceController(mainFrame, dlgModel);
                    dlgController.getDialog().addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            updateSlotInterfacesTable();
                        }
                    });
                    dlgController.show();
                }
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
                    dlgModel.setDeviceSlots(new ArrayList<DeviceSlot>());
                    dlgModel.setName("");
                    dlgModel.setDesc("");
                    dlgModel.setSelectedDeviceId(deviceId);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Внимание!", JOptionPane.ERROR_MESSAGE);
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

        removeDeviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<SlotInterface> slotInterfaces = new ArrayList<>();
                try {
                    slotInterfaces = storageRepo.getSlotInterfaces(getSelectedDeviceId(), 0);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return;
                }
                int input = JOptionPane.YES_OPTION;
                if (slotInterfaces.size() > 0) {
                    input = JOptionPane.showConfirmDialog(
                            null
                            , "У устройства найдены интерфейсы! Удалить устройство и интерфейсы?"
                            , "Select"
                            , JOptionPane.YES_NO_OPTION
                            , JOptionPane.WARNING_MESSAGE);
                }
                if (input == JOptionPane.YES_OPTION) {
                    try {
                        storageRepo.removeDevice(getSelectedDeviceId());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    updateDevicesTable();
                }
            }
        });

        addDeviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDeviceModel dlgModel = new AddDeviceModel();
                dlgModel.setSelectedServerBoxId(getSelectedServerBoxId());
                dlgModel.setName("");
                dlgModel.setNum(1);
                dlgModel.setSize(1);
                dlgModel.setServerBoxes(new ArrayList<ServerBox>());

                AddDeviceController dlgController = new AddDeviceController(mainFrame, dlgModel);
                dlgController.getDialog().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        updateDevicesTable();
                    }
                });
                dlgController.show();
            }
        });

        linkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int interfaceId;
                try {
                    interfaceId = getSelectedSlotInterfaceId();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Внимание!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LinkModel dlgModel = new LinkModel();
                List<SlotInterface> slotInterfaces = new ArrayList<>();
                try {
                    slotInterfaces = storageRepo.getSlotInterfaces(0, interfaceId);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Внимание!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (slotInterfaces.size() != 1) {
                    JOptionPane.showMessageDialog(null, "Интерфейс не определен", "Внимание!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dlgModel.setSlotInterface(slotInterfaces.get(0));
                dlgModel.setTargetSlotInterface(null);
                if (dlgModel.getSlotInterface().getLinkId() > 0) {
                    try {
                        dlgModel.setTargetSlotInterface(storageRepo.getSlotInterfaceByLink(dlgModel.getSlotInterface().getLinkId(), dlgModel.getSlotInterface().getId()));
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Внимание!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                LinkController dlgController = new LinkController(mainFrame, dlgModel);
                dlgController.getDialog().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        updateSlotInterfacesTable();
                    }
                });
                dlgController.show();
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

    private int getSelectedSlotInterfaceId() throws Exception {
        int row = tblInterfaces.getSelectedRow();
        if (row == -1) {
            throw new Exception("Не выбран интерфейс");
        }

        return (int) tblInterfaces.getModel().getValueAt(row, 0);
    }

    private void updateDevicesTable() {
        DefaultTableModel tableModel = (DefaultTableModel) tblDevices.getModel();
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        List<Device> data = new ArrayList<>();
        try {
            data = storageRepo.getDevices(getSelectedServerBoxId(), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Device item : data) {
            String deviceNumStr = "";
            if (item.getNum() != null && item.getNum() > 0) {
                deviceNumStr = Integer.toString(item.getNum());
            }
            tableModel.addRow(new Object[]{item.getId(), item.getServerBox().getName(), deviceNumStr, Integer.toString(item.getSize()), item.getName(), item.getDesc()});
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
            data = storageRepo.getSlotInterfaces(getSelectedDeviceId(), 0);
            data = storageRepo.getFillLinkedSlotInterfaces(data);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (SlotInterface item : data) {
            //String linkId = "";
            String linkInfo = "";
            if (item.getLinkId() != null && item.getLinkId() > 0) {
                //linkId = Integer.toString(item.getLinkId());
                if (item.getLinkSlotInterface() != null) {
                    SlotInterface target = item.getLinkSlotInterface();
                    linkInfo =
                            target.getInterfaceType().getName() + ": " +
                                    "[" + target.getDeviceSlot().getDevice().getServerBox().getName() + "] " +
                                    + target.getDeviceSlot().getDevice().getNum() + ". "
                                    + target.getDeviceSlot().getName() + " -> "
                                    + target.getName();
                }
            }
            tableModel.addRow(new Object[]{item.getId(), item.getDeviceSlot().getName(), item.getName()/*, linkId*/, linkInfo, item.getDesc()});
        }
    }

}
