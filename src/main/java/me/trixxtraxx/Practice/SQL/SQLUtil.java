package me.trixxtraxx.Practice.SQL;

import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.Utils;

import java.lang.reflect.Constructor;
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

    public Map getMap(String Name)
    {
        try
        {
            Map m = null;
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Map WHERE Map.MapName = ?");
            ps.setString(0, Name);

            ResultSet res = ps.executeQuery();
            if (res.next())
            {
                Class<?> clazz = Class.forName(res.getString("SpawnClass"));
                ISpawnComponent spawn = (ISpawnComponent) clazz.newInstance();
                spawn.applyData(res.getString("SpawnData"));
                m = new Map(res.getInt("Map_ID"), res.getString("MapName"), res.getString("load"), spawn);
            }
            ps.close();
            res.close();

            return m;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void applyComponents(Map m, GameLogic logic)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM MapComponent INNER JOIN Map ON Map.Map_ID = MapComponent.Map_ID WHERE Map.Map_ID = ?");
            ps.setInt(0, m.getSqlIndex());

            ResultSet res = ps.executeQuery();
            while (res.next())
            {
                Class<?> clazz = Class.forName(res.getString("Class"));
                Constructor<?> constructor = clazz.getConstructor(GameLogic.class, String.class);
                constructor.newInstance(logic, res.getString("Data"));
            }
            ps.close();
            res.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteMap(Map m)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Map WHERE Map.Map_ID = ?");
            ps.setInt(0, m.getSqlIndex());

            ps.executeUpdate();
            ps.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addMap(Map m)
    {
        try
        {
            if(true)
            {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Map (MapName, load, SpawnClass, SpawnData) VALUES (?,?,?,?)");
                ps.setString(0, m.getName());
                ps.setString(1, m.getLoad());
                ps.setString(2, m.getSpawn().getClass().getName());
                ps.setString(3, m.getSpawn().getData());

                ps.executeUpdate();
                ps.close();
            }
            if(true){
                Statement s = con.createStatement();
                for (MapComponent comp:m.getComponents())
                {
                    s.addBatch("INSERT INTO MapComponent (Map_ID, Class, Data) VALUES (" + m.getSqlIndex() + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
                }
                s.executeBatch();
                s.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
