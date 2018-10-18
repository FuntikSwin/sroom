package sroot_pkg.domain.concrete;

import sroot_pkg.domain.abstr.IStorageRepo;
import sroot_pkg.domain.model.Device;
import sroot_pkg.domain.model.ServerBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbStorageRepo implements IStorageRepo {

    private final String driverName = "org.sqlite.JDBC";
    private final String connStr = "jdbc:sqlite:/home/fomakin/Projects/SqLiteDB/sroom_db";
    private Connection connection;

    public DbStorageRepo() {
        connection = null;
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

    public List<Device> getDevices() {
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

        String sql = "select d.Id, d.Name, d.Num, d.Size, d.ServerBoxId, sb.Name ServerBoxName from Device d left join ServerBox sb ON sb.Id = d.ServerBoxId";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                Integer tmp = resultSet.getInt("ServerBoxId");
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

        return data;
    }
}
