package sroom_pkg.domain.concrete;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import sroom_pkg.domain.abstr.IReportRepo;
import sroom_pkg.domain.model.Device;
import sroom_pkg.domain.model.DeviceSlot;
import sroom_pkg.domain.model.ServerBox;
import sroom_pkg.domain.model.SlotInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelReportRepo implements IReportRepo {
    @Override
    public void generalReport(List<SlotInterface> slotInterfaces) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet1 = workbook.createSheet("General report all");

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        int rowNum = 1;
        Row row = sheet1.createRow(rowNum);
        row.createCell(0).setCellValue("General report all");
        rowNum++;
        rowNum++;

        int colNum = 0;
        row = sheet1.createRow(rowNum);
        row.createCell(colNum).setCellValue("Шкаф");
        colNum++;
        row.createCell(colNum).setCellValue("Устройство №");
        colNum++;
        row.createCell(colNum).setCellValue("Устройство");
        colNum++;
        row.createCell(colNum).setCellValue("Описание");
        colNum++;
        row.createCell(colNum).setCellValue("Слот");
        colNum++;
        row.createCell(colNum).setCellValue("Интерфейс");
        colNum++;
        row.createCell(colNum).setCellValue("Прим.");
        colNum++;
        row.createCell(colNum).setCellValue("Link");

        rowNum++;
        for (SlotInterface currInterface: slotInterfaces) {
            row = sheet1.createRow(rowNum);
            colNum = 0;
            row.createCell(colNum).setCellValue(currInterface.getDeviceSlot().getDevice().getServerBox().getName());
            colNum++;
            row.createCell(colNum).setCellValue(currInterface.getDeviceSlot().getDevice().getNum());
            colNum++;
            row.createCell(colNum).setCellValue(currInterface.getDeviceSlot().getDevice().getName());
            colNum++;
            row.createCell(colNum).setCellValue(currInterface.getDeviceSlot().getDevice().getDesc());
            colNum++;
            row.createCell(colNum).setCellValue(currInterface.getDeviceSlot().getName());
            colNum++;
            row.createCell(colNum).setCellValue(currInterface.getName());
            colNum++;
            row.createCell(colNum).setCellValue(currInterface.getDesc());
            colNum++;
            row.createCell(colNum).setCellValue(currInterface.getLinkSlotInterfacePath());

            rowNum++;
        }

        sheet1.autoSizeColumn(0);
        sheet1.autoSizeColumn(1);
        sheet1.autoSizeColumn(2);
        sheet1.autoSizeColumn(4);
        sheet1.autoSizeColumn(5);
        sheet1.autoSizeColumn(7);

        HSSFSheet sheet2 = workbook.createSheet("General report group");

        rowNum = 1;
        row = sheet2.createRow(rowNum);
        row.createCell(0).setCellValue("General report group");
        rowNum++;
        rowNum++;

        colNum = 0;
        row = sheet2.createRow(rowNum);
        row.createCell(colNum).setCellValue("Слот");
        colNum++;
        row.createCell(colNum).setCellValue("Интрефейс");
        colNum++;
        row.createCell(colNum).setCellValue("Прим.");
        colNum++;
        row.createCell(colNum).setCellValue("Link");

        HashMap<ServerBox, List<Device>> sbMap = new HashMap<>();
        for (SlotInterface currInt: slotInterfaces) {
            ServerBox currServerBox = currInt.getDeviceSlot().getDevice().getServerBox();
            Device currDevice = currInt.getDeviceSlot().getDevice();
            if (!sbMap.containsKey(currServerBox)) {
                List<Device> data = new ArrayList<>();
                data.add(currDevice);
                sbMap.put(currServerBox, data);
            } else {
                sbMap.get(currServerBox).add(currDevice);
            }
        }

        HashMap<Device, List<DeviceSlot>> deviceMap = new HashMap<>();
        for (SlotInterface currInt: slotInterfaces) {
            Device currDevice = currInt.getDeviceSlot().getDevice();
            DeviceSlot currSlot = currInt.getDeviceSlot();
            if (!deviceMap.containsKey(currDevice)) {
                List<DeviceSlot> data = new ArrayList<>();
                data.add(currSlot);
                deviceMap.put(currDevice, data);
            } else {
                if (!deviceMap.get(currDevice).contains(currSlot)) {
                    deviceMap.get(currDevice).add(currSlot);
                }
            }
        }

        HashMap<DeviceSlot, List<SlotInterface>> slotMap = new HashMap<>();
        for (SlotInterface currInt: slotInterfaces) {
            DeviceSlot currSlot = currInt.getDeviceSlot();
            if (!slotMap.containsKey(currSlot)) {
                List<SlotInterface> data = new ArrayList<>();
                data.add(currInt);
                slotMap.put(currSlot, data);
            } else {
                slotMap.get(currSlot).add(currInt);
            }
        }

        for (ServerBox currSb: sbMap.keySet()) {
            List<Device> devices = sbMap.get(currSb);
            Collections.sort(devices, new Comparator<Device>() {
                @Override
                public int compare(Device o1, Device o2) {
                    return o1.getNum().compareTo(o2.getNum());
                }
            });

            for (Device currDevice: devices) {
                row = sheet2.createRow(rowNum);
                row.createCell(0).setCellValue(currSb.getName() + "; №" + currDevice.getNum() + " " + currDevice.getName());
                row.getCell(0).setCellStyle(cellStyle);
                rowNum++;
                row = sheet2.createRow(rowNum);
                row.createCell(0).setCellValue(currDevice.getDesc());
                row.getCell(0).setCellStyle(cellStyle);
                rowNum++;

                List<DeviceSlot> deviceSlots = deviceMap.get(currDevice);
                Collections.sort(deviceSlots, new Comparator<DeviceSlot>() {
                    @Override
                    public int compare(DeviceSlot o1, DeviceSlot o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                for (DeviceSlot currSlot: deviceSlots) {
                    List<SlotInterface> interfaces = slotMap.get(currSlot);
                    /*Collections.sort(interfaces, new Comparator<SlotInterface>() {
                        @Override
                        public int compare(SlotInterface o1, SlotInterface o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });*/
                    Collections.sort(interfaces, (o1, o2) -> {
                        String x1 = o1.getDeviceSlot().getName();
                        String x2 = o2.getDeviceSlot().getName();
                        int sComp = x1.compareTo(x2);

                        if (sComp != 0) {
                            return sComp;
                        }

                        int i1 = 0;
                        int i2 = 0;

                        String str = "";
                        Pattern p = Pattern.compile("-?\\d+");
                        Matcher m = p.matcher(o1.getName());
                        while (m.find()) {
                            str = str + m.group();
                        }
                        if (str != "") {
                            i1 = Integer.parseInt(str);
                        }

                        str = "";
                        m = p.matcher(o2.getName());
                        while (m.find()) {
                            str = str + m.group();
                        }
                        if (str != "") {
                            i2 = Integer.parseInt(str);
                        }

                        return i1-i2;
                    });

                    for (SlotInterface currInt: interfaces) {
                        colNum = 0;
                        row = sheet2.createRow(rowNum);
                        row.createCell(colNum).setCellValue(currSlot.getName());
                        colNum++;
                        row.createCell(colNum).setCellValue(currInt.getName());
                        colNum++;
                        row.createCell(colNum).setCellValue(currInt.getDesc());
                        colNum++;
                        row.createCell(colNum).setCellValue(currInt.getLinkSlotInterfacePath());
                        rowNum++;
                    }
                }
            }
        }

        sheet2.autoSizeColumn(0);
        sheet2.autoSizeColumn(1);
        sheet2.autoSizeColumn(3);

        try (FileOutputStream out = new FileOutputStream(new File("/home/fomakin/Projects/Idea/sroom_reports/general.xls"))) {
            workbook.write(out);
        }
    }
}
