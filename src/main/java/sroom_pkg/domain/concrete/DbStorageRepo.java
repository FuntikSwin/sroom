package sroom_pkg.domain.concrete;

import sroom_pkg.domain.abstr.IStorageRepo;
import sroom_pkg.domain.model.Device;
import sroom_pkg.domain.model.DeviceSlot;
import sroom_pkg.domain.model.ServerBox;
import sroom_pkg.domain.model.SlotInterface;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbStorageRepo implements IStorageRepo {

    private final String driverName = "org.sqlite.JDBC";
    //private final String connStr = "jdbc:sqlite:/home/fomakin/Projects/SqLiteDB/sroom_db";
    private final String connStr;
    private Connection connection;

    public DbStorageRepo() {
        connection = null;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sroom_db").getFile());
        connStr = "jdbc:sqlite:" + file.getAbsolutePath();
    }

    private void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);

        connection = null;
        connection = DriverManager.getConnection(connStr);
    }

    private void closeConnection() throws SQLException {
        connection.close();
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

    public List<Device> getDevices(int serverBoxId) throws SQLException {
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

        String sql = "select d.Id, d.Name, d.Num, d.Size, d.ServerBoxId, sb.Name ServerBoxName " +
                "from Device d " +
                "left join ServerBox sb ON sb.Id = d.ServerBoxId " +
                "where d.ServerBoxId = case when " + Integer.toString(serverBoxId) + " = 0 then d.ServerBoxId else " + Integer.toString(serverBoxId) + " end";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                Device item = new Device(resultSet.getInt("Id")
                        , resultSet.getString("Name")
                        , resultSet.getInt("Num")
                        , 0
                        , resultSet.getInt("ServerBoxId"));
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

    public List<SlotInterface> getSlotInterfaces(int deviceId) throws SQLException {
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

        String sql = "select i.Id, i.SlotId, i.Name, i.LinkId, s.Name SlotName, s.DeviceId " +
                "from SlotInterface i " +
                "left join DeviceSlot s ON s.Id = i.SlotId " +
                "where s.DeviceId = " + Integer.toString(deviceId);
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                SlotInterface item = new SlotInterface(
                        resultSet.getInt("Id")
                        , resultSet.getInt("SlotId")
                        , resultSet.getString("Name")
                        , resultSet.getInt("LinkId"));
                if (item.getSlotId() != null) {
                    item.setDeviceSlot(new DeviceSlot(
                            item.getSlotId()
                            , resultSet.getString("SlotName")
                            , resultSet.getInt("DeviceId")));
                }
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return data;
    }
}
