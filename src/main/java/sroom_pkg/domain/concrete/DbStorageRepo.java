package sroom_pkg.domain.concrete;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.model.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbStorageRepo implements IStorageRepo {

    private final String driverName = "org.sqlite.JDBC";
    private String connStr;
    private Connection connection;

    private String fileDbName = "";

    public DbStorageRepo() {
        connection = null;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sroom_db").getFile());
        fileDbName = file.getName();
        connStr = "jdbc:sqlite:" + file.getAbsolutePath();
        //connStr = "jdbc:sqlite:/home/fomakin/Projects/SqLiteDB/sroom_db";
    }

    private void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);

        connection = null;
        connection = DriverManager.getConnection(connStr);
    }

    private void closeConnection() throws SQLException {
        connection.close();
    }

    @Override
    public String getDbFileName() {
        return fileDbName;
    }

    @Override
    public void setDb(File fileDb) {
        fileDbName = fileDb.getName();
        connStr = "jdbc:sqlite:" + fileDb.getAbsolutePath();
    }

    public List<ServerBox> getServerBoxes() throws SQLException {
        List<ServerBox> data = new ArrayList<ServerBox>();

        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String sql = "select * from ServerBox";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                data.add(new ServerBox(resultSet.getInt("Id"), resultSet.getString("Name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return data;
    }

    @Override
    public void addServerBox(String name) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "insert into ServerBox(Name) values(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }

        closeConnection();
    }

    public List<Device> getDevices(int serverBoxId, int deviceId) throws SQLException {
        List<Device> data = new ArrayList<>();

        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String sql = "select d.Id, d.Name, d.Num, d.Size, d.ServerBoxId, sb.Name ServerBoxName, d.Desc " +
                "from Device d " +
                "left join ServerBox sb ON sb.Id = d.ServerBoxId " +
                "where d.ServerBoxId = case when " + Integer.toString(serverBoxId) + " = 0 then d.ServerBoxId else " + Integer.toString(serverBoxId) + " end " +
                "and d.Id = case when " + Integer.toString(deviceId) + " = 0 then d.Id else " + Integer.toString(deviceId) + " end " +
                "order by d.ServerBoxId, d.Num";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                Device item = new Device(resultSet.getInt("Id")
                        , resultSet.getString("Name")
                        , resultSet.getInt("Num")
                        , resultSet.getInt("Size")
                        , resultSet.getInt("ServerBoxId")
                        , resultSet.getString("Desc"));
                if (item.getServerBoxId() != null) {
                    item.setServerBox(new ServerBox(item.getServerBoxId(), resultSet.getString("ServerBoxName")));
                }
                data.add(item);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return data;
    }

    @Override
    public void removeDevice(int deviceId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "delete from SlotInterface " +
                "where SlotId in ( " +
                "select s.Id " +
                "from DeviceSlot s " +
                "where s.DeviceId = ?" +
                ")";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, deviceId);
            pstmt.executeUpdate();
        }
        sql = "delete from DeviceSlot where DeviceId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, deviceId);
            pstmt.executeUpdate();
        }
        sql = "delete from Device where Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, deviceId);
            pstmt.executeUpdate();
        }

        closeConnection();
    }

    @Override
    public void addDevice(String name, Integer num, int size, int serverBoxId, String desc) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        int deviceId = 0;
        String sql = "insert into Device(Name, Num, 'Size', ServerBoxId, Desc) values(?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            if (num != null) {
                pstmt.setInt(2, num);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setInt(3, size);
            pstmt.setInt(4, serverBoxId);
            pstmt.setString(5, desc);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return;
            }
            try (ResultSet genKeys = pstmt.getGeneratedKeys()) {
                if (genKeys.next()) {
                    deviceId = genKeys.getInt(1);
                }
            }
        }

        if (deviceId > 0) {
            sql = "insert into DeviceSlot(Name, DeviceId) values(?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, "Default");
                pstmt.setInt(2, deviceId);
                pstmt.executeUpdate();
            }
        }

        closeConnection();
    }

    @Override
    public void updateDevice(int deviceId, String name, Integer num, int size, int serverBoxId, String desc) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "update Device " +
                "set Name = ? " +
                "  , Num = ? " +
                "  , 'Size' = ? " +
                "  , ServerBoxId = ? " +
                "  , 'Desc' = ? " +
                "where Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, num);
            pstmt.setInt(3, size);
            pstmt.setInt(4, serverBoxId);
            pstmt.setString(5, desc);
            pstmt.setInt(6, deviceId);
            pstmt.executeUpdate();
        }

        closeConnection();
    }

    public List<SlotInterface> getSlotInterfaces(int deviceId, int slotInterfaceId) throws SQLException {
        List<SlotInterface> data = new ArrayList<>();

        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        //if (deviceId > 0 || slotInterfaceId > 0) {
            String sql = "select i.Id, i.SlotId, i.Name, i.LinkId, i.Desc, s.Name SlotName, s.DeviceId, d.Name DeviceName, d.Num DeviceNum, d.Desc DeviceDesc, d.ServerBoxId, sb.Name ServerBoxName, l.InterfaceTypeId, it.Name InterfaceTypeName " +
                    "from SlotInterface i " +
                    "left join DeviceSlot s ON s.Id = i.SlotId " +
                    "left join Device d ON d.Id = s.DeviceId " +
                    "left join ServerBox sb ON sb.Id = d.ServerBoxId " +
                    "left join Link l ON l.Id = i.LinkId " +
                    "left join InterfaceType it on it.Id = l.InterfaceTypeId " +
                    "where s.DeviceId = case when " + Integer.toString(deviceId) + " = 0 then s.DeviceId else " + Integer.toString(deviceId) + " end " +
                    "and i.Id = case when " + Integer.toString(slotInterfaceId) + " = 0 then i.Id else " + Integer.toString(slotInterfaceId) + " end " +
                    "order by i.Name";
            try (Statement stmt = connection.createStatement();
                 ResultSet resultSet = stmt.executeQuery(sql)) {
                while (resultSet.next()) {
                    SlotInterface item = new SlotInterface(
                            resultSet.getInt("Id")
                            , resultSet.getInt("SlotId")
                            , resultSet.getString("Name")
                            , resultSet.getInt("LinkId")
                    , resultSet.getString("Desc"));
                    if (item.getSlotId() != null) {
                        DeviceSlot itemSlot = new DeviceSlot(
                                item.getSlotId()
                                , resultSet.getString("SlotName")
                                , resultSet.getInt("DeviceId"));
                        if (itemSlot.getDeviceId() != null) {
                            Device itemDevice = new Device(
                                    itemSlot.getDeviceId()
                                    , resultSet.getString("DeviceName")
                                    , resultSet.getInt("DeviceNum")
                                    , 0
                                    , resultSet.getInt("ServerBoxId")
                                    , resultSet.getString("DeviceDesc"));
                            if (itemDevice.getServerBoxId() != null) {
                                ServerBox itemBox = new ServerBox(
                                        itemDevice.getServerBoxId()
                                        , resultSet.getString("ServerBoxName"));
                                itemDevice.setServerBox(itemBox);
                            }

                            itemSlot.setDevice(itemDevice);
                        }

                        item.setDeviceSlot(itemSlot);
                    }
                    if (item.getLinkId() != null && item.getLinkId() > 0) {
                        InterfaceType interfaceType = new InterfaceType(resultSet.getInt("InterfaceTypeId"), resultSet.getString("InterfaceTypeName"));
                        item.setInterfaceType(interfaceType);
                    }
                    data.add(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        //}

        Collections.sort(data, (o1, o2) -> {
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

        closeConnection();

        return data;
    }

    public void removeSlotInterface(int slotInterfaceId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "delete from SlotInterface where Id = " + Integer.toString(slotInterfaceId);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }

        closeConnection();
    }

    public void addSlotInterface(int deviceSlotId, String name, String desc, Integer linkId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "insert into SlotInterface(SlotId, Name, LinkId, Desc) values(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, deviceSlotId);
            pstmt.setString(2, name);
            if (linkId == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, linkId);
            }
            pstmt.setString(4, desc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();
    }

    @Override
    public void updateSlotInterface(int slotInterfaceId, int deiceSlotId, String name, String desc, Integer linkId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "update SlotInterface " +
                "set SlotId = ? " +
                ", 'Name' = ? " +
                ", LinkId = ? " +
                ", Desc = ? " +
                "where Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, deiceSlotId);
            pstmt.setString(2, name);
            if (linkId != null && linkId > 0) {
                pstmt.setInt(3, linkId);
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, desc);
            pstmt.setInt(5, slotInterfaceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();
    }

    @Override
    public List<DeviceSlot> getDeviceSlots(int deviceId) throws SQLException {
        List<DeviceSlot> data = new ArrayList<>();
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String sql = "select s.Id, s.Name, s.DeviceId from DeviceSlot s where s.DeviceId = " + Integer.toString(deviceId);
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                data.add(new DeviceSlot(
                        resultSet.getInt("Id")
                        , resultSet.getString("Name")
                        , resultSet.getInt("DeviceId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();
        return data;
    }

    @Override
    public void addDeviceSlot(String name, int deviceId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "insert into DeviceSlot(Name, DeviceId) values(?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, deviceId);

            pstmt.executeUpdate();
        }

        closeConnection();
    }

    @Override
    public void updateDeviceSlot(int deviceSlotId, String name) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "update DeviceSlot set Name = ? where Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, deviceSlotId);
            pstmt.executeUpdate();
        }

        closeConnection();
    }

    @Override
    public List<InterfaceType> getInterfaceTypes() throws SQLException {
        List<InterfaceType> data = new ArrayList<>();
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String sql = "select t.Id, t.Name from InterfaceType t";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                data.add(new InterfaceType(resultSet.getInt("Id"), resultSet.getString("Name")));
            }
        }

        closeConnection();
        return data;
    }

    @Override
    public SlotInterface getSlotInterfaceByLink(int linkId, int sourceSlotInterfaceId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        SlotInterface targetSlotINterface = null;

        int targetSlotInterfaceId = 0;
        String sql = "select i.Id " +
                "from SlotInterface i " +
                "where i.LinkId = ? " +
                "and i.Id <> ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, linkId);
            pstmt.setInt(2, sourceSlotInterfaceId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    targetSlotInterfaceId = resultSet.getInt(1);
                }
            }
        }
        closeConnection();

        if (targetSlotInterfaceId > 0) {
            List<SlotInterface> slotInterfaces = getSlotInterfaces(0, targetSlotInterfaceId);
            if (slotInterfaces.size() == 1) {
                targetSlotINterface = slotInterfaces.get(0);
            }
        }

        return targetSlotINterface;
    }

    @Override
    public List<SlotInterface> getFillLinkedSlotInterfaces(List<SlotInterface> sources) throws SQLException {

        List<SlotInterface> result = new ArrayList<>();
        for (SlotInterface item: sources) {
            SlotInterface resItem = item;
            if (resItem.getLinkId() != null && resItem.getLinkId() > 0) {
                resItem.setLinkSlotInterface(getSlotInterfaceByLink(resItem.getLinkId(), resItem.getId()));
            }
            result.add(resItem);
        }

        return result;
    }

    @Override
    public void addLink(int sourceSlotInterfaceId, int targetInterfaceId, int interfaceTypeId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        int linkId = 0;
        String sql = "insert into Link(InterfaceTypeId) values(?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, interfaceTypeId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet idents = pstmt.getGeneratedKeys()) {
                    if (idents.next()) {
                        linkId = idents.getInt(1);
                    }
                }
            }
        }

        if (linkId > 0) {
            sql = "update SlotInterface set LinkId = ? where Id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, linkId);
                pstmt.setInt(2, sourceSlotInterfaceId);
                pstmt.executeUpdate();
            }
            sql = "update SlotInterface set LinkId = ? where Id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, linkId);
                pstmt.setInt(2, targetInterfaceId);
                pstmt.executeUpdate();
            }
        }

        closeConnection();
    }

    @Override
    public void updateLink(int linkId, int interfaceTypeId, int sourceSlotInterfaceId, int targetSlotInterfaceId) throws SQLException {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String sql = "update Link set InterfaceTypeId = ? where Id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, interfaceTypeId);
            pstmt.setInt(2, linkId);
            pstmt.executeUpdate();
        }
        sql = "update SlotInterface set LinkId = null where LinkId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, linkId);
            pstmt.executeUpdate();
        }
        sql = "update SlotInterface set LinkId = ? where Id in (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, linkId);
            pstmt.setInt(2, sourceSlotInterfaceId);
            pstmt.setInt(3, targetSlotInterfaceId);
            pstmt.executeUpdate();
        }

        closeConnection();
    }
}
