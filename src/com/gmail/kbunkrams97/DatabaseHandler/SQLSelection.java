package com.gmail.kbunkrams97.DatabaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.gmail.kbunkrams97.SQLMain;

public class SQLSelection {

    private static Connection con;
    
    private static SQLMain plugin = SQLMain.getInstance();

    private static String address = (String) plugin.getConfig().get("MySQL.Address");
    private static String database = (String) plugin.getConfig().get("MySQL.Database");
    private static String username = (String) plugin.getConfig().get("MySQL.Username");
    private static String password = (String) plugin.getConfig().get("MySQL.Password");
    private static int port = plugin.getConfig().getInt("MySQL.Port");

    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        String prefix = "[SQL-Log] ";
        if (plugin.getConfig().getBoolean("MySQL.Enabled")) {
            try 
            {
                Class.forName("com.mysql.jdbc.Driver");
                if (con != null && con.isClosed() == false) {}
                else 
                {
                    con = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, username, password);
                }
            } catch (SQLException | ClassNotFoundException e) {
                plugin.log.info(prefix + "MySQL connection failed. Switching to SQLite.");
                plugin.getConfig().set("MySQL.Enabled", false);
                plugin.saveConfig();
                plugin.firstLoad = true;
                con = getConnection();
            }
            
            if(plugin.firstLoad == true)
            {
            	plugin.log.info(prefix + "Connection to MySQL database was successful.");
            	plugin.firstLoad = false;
            }
        }
        else 
        {
            try {
                Class.forName("org.sqlite.JDBC");
                if (con != null && con.isClosed() == false) {}
                else 
                {
                    con = DriverManager.getConnection("jdbc:sqlite:plugins/SQL-Log/Data.sql");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            if(plugin.firstLoad == true)
            {
            	plugin.log.info(prefix + "Connection SQLite database was successful.");
            	plugin.firstLoad = false;
            }
        }
        return con;
    }

    public static Statement getStatement() throws SQLException, ClassNotFoundException {
        if (plugin.getConfig().getBoolean("MySQL.Enabled")) 
        {
            return getConnection().createStatement();
        } 
        else 
        {
            return getConnection().createStatement();
        }
    }
}
