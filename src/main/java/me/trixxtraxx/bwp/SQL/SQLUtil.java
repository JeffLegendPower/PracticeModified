package me.trixxtraxx.bwp.SQL;

import me.trixxtraxx.bwp.Map.Map;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUtil
{
    public static SQLUtil Instance = new SQLUtil();

    private SQLUtil() {}

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    private Connection con;

    public void init(String host, String port, String database, String username, String password)
    {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        try {
            connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean isConnected()
    {
        return con != null;
    }


    public void connect() throws SQLException
    {
        if(!isConnected())
        {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", username, password);
        }
    }

    /*public List<Map> getMaps()
    {
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement("SELECT * FROM Map");

            ResultSet res = ps.executeQuery();
            List<Map> maps = new ArrayList<>();
            while (res.next())
            {
                maps.add(new Map(
                        res.getString("Name"),
                        res.getString("LoadName"),
                        res.getString("GamemodeData"),
                        res.getString("ImplementationClass"),
                        res.getString("ImplementationData"),
                        res.getString("SpecificClass"),
                        res.getString("SpecificData")
                ));
            }
            return maps;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }*/
}
