package me.trixxtraxx.Practice.SQL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;

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

    public PracticePlayer getPlayer(Player p)
    {
        try
        {
            int PlayerId = -1;
            if (true)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Player WHERE Name = ? OR UUID = ?");
                ps.setString(0, p.getName());
                ps.setString(1, p.getUniqueId().toString());

                ResultSet res = ps.executeQuery();
                if (res.next())
                {
                    PlayerId = res.getInt("Player_ID");
                }
                else
                {
                    PlayerId = createPlayer(p);
                }
                ps.close();
                res.close();
            }
            if(true)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM KitOrder INNER JOIN PlayerKitOrder ON KitOrder.KitOrder_ID = PlayerKitOrder.KitOrder_ID INNER JOIN Player ON PlayerKitOrder.Player_ID = Player.Player_ID WHERE Player.Player_ID = ?");
                ps.setString(0, PlayerId + "");

                HashMap<Integer, HashMap<Integer, Integer>> orders = new HashMap<>();

                ResultSet res = ps.executeQuery();
                Gson gson = new Gson();
                //new instance for the getClass(), didnt find another way
                HashMap<Integer, Integer> map = new HashMap<>();
                if (res.next())
                {
                    orders.put(res.getInt("Kit_ID"), gson.fromJson(res.getString("Order"), map.getClass()));
                }

                ps.close();
                res.close();
                return new PracticePlayer(PlayerId, p, orders);
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private int createPlayer(Player p)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Player (UUID, Name) VALUES (?,?)");
            ps.setString(0, p.getUniqueId() + "");
            ps.setString(1, p.getName());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id =  rs.getInt(1);

            ps.close();
            rs.close();
            return id;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public void addOrder(PracticePlayer prac, HashMap<Integer, Integer> order, int kitId, boolean nullkitid)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO KitOrder (Order, Kit_ID) VALUES (?,?)");
            ps.setString(0, new Gson().toJson(order));
            if(nullkitid) ps.setNull(1, Types.NULL);
            else ps.setInt(1, kitId);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id =  rs.getInt(1);
            ps.close();

            ps = con.prepareStatement("INSERT INTO PlayerKitOrder (Player_ID, KitOrder_ID) VALUES (?,?)");
            ps.setInt(0, prac.getPlayerId());
            ps.setInt(1, id);

            ps.close();
            rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Kit getKit(int KitId)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Kit WHERE Kit.Kit_ID = ?");
            ps.setString(0, KitId + "");

            ResultSet res = ps.getResultSet();

            //new Instance to get List class
            List<ItemStack> items= new ArrayList<>();
            int deforder = 0;
            if(res.next())
            {
                items = new Gson().fromJson(res.getString("Items"), items.getClass());
                deforder = res.getInt("defaultOrder");
            }
            else
            {
                ps.close();
                res.close();
                return null;
            }

            ps.close();
            res.close();
            ps = con.prepareStatement("SELECT * FROM KitOrder INNER JOIN Kit ON Kit.defaultOrder = KitOrder.KitOrder_ID WHERE Kit.defaultOrder = ?");
            ps.setInt(0, deforder);

            res = ps.getResultSet();

            HashMap<Integer, Integer> defaultOrder = new HashMap<>();
            if(res.next())
            {
                defaultOrder = new Gson().fromJson(res.getString("Order"), defaultOrder.getClass());
            }
            else
            {
                ps.close();
                res.close();
                return null;
            }

            ps.close();
            res.close();
            return new Kit(KitId,items, defaultOrder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void applyComponents(Kit k)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM KitComponent INNER JOIN Kit ON Kit.Kit_ID = KitComponent.Kit_ID WHERE KitComponent.Kit_ID = ?");
            ps.setInt(0, k.getSqlId());

            ResultSet res = ps.getResultSet();

            while (res.next())
            {
                Class<?> clazz = Class.forName(res.getString("Class"));
                Constructor<?> constructor = clazz.getConstructor(Kit.class, String.class);
                constructor.newInstance(k, res.getString("Data"));
            }

            ps.close();
            res.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}