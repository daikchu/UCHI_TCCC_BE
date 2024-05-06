package com.vn.osp.notarialservices.masterConvert.connectDBMaster;

import com.vn.osp.notarialservices.masterConvert.dto.ConfigDBMaster;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import org.apache.log4j.Logger;

public class ConnectUtils {
    public static Connection conn = null;
    private static final Logger LOG = Logger.getLogger(ConnectUtils.class);

    public Connection connect(ConfigDBMaster configDBMaster) {
        String master_dbName = configDBMaster.getMaster_dbName();
        String master_serverip = configDBMaster.getMaster_serverip();
        String master_serverport = configDBMaster.getMaster_serverport();
        String url = "jdbc:jtds:sqlserver://" + master_serverip + ":" + master_serverport + ";databaseName=" + master_dbName + "";
        String master_driver = configDBMaster.getMaster_driver();
        String master_databaseUserName = configDBMaster.getMaster_databaseUserName();
        byte[] decodedBytes = Base64.getDecoder().decode(configDBMaster.getMaster_databasePassword());
        String master_databasePassword = new String(decodedBytes);

        try {
            Class.forName(master_driver).newInstance();
            conn = DriverManager.getConnection(url, master_databaseUserName, master_databasePassword);
        } catch (Exception e) {
            LOG.error("loi xay ra o ham ConnectUtils.connect");
        }
        return conn;
    }

    public void close() throws SQLException {
        conn.close();
    }
}
