package me.trixxtraxx.Practice.SQL;

import com.google.gson.Gson;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.StatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.IStatComponent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Kit.KitComponent;
import me.trixxtraxx.Practice.Map.ISpawnComponent;
import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    public void applyComponents(Kit k)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM KitComponent INNER JOIN Kit ON Kit.Kit_ID = KitComponent.Kit_ID WHERE KitComponent.Kit_ID = ?");
            ps.setInt(1, k.getSqlId());
            
            ps.executeQuery();
            
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
    public void applyComponents(GameLogic logic)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM GameComponent INNER JOIN Gamemode ON GameComponent.Gamemode_ID = Gamemode.Gamemode_ID WHERE GameComponent.Gamemode_ID = ?");
            ps.setInt(1, logic.getId());
            
            ps.executeQuery();
            
            ResultSet res = ps.getResultSet();
            
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
    public void applyComponents(Map m)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM MapComponent INNER JOIN Map ON Map.Map_ID = MapComponent.Map_ID WHERE Map.Map_ID = ?");
            ps.setInt(1, m.getSqlIndex());
            
            ResultSet res = ps.executeQuery();
            while (res.next())
            {
                Class<?> clazz = Class.forName(res.getString("Class"));
                Constructor<?> constructor = clazz.getConstructor(Map.class, String.class);
                constructor.newInstance(m, res.getString("Data"));
            }
            ps.close();
            res.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Map getMap(String Name)
    {
        try
        {
            Map m = null;
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Map WHERE Map.MapName = ?");
            ps.setString(1, Name);

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
    public void deleteMap(Map m)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Map WHERE Map.Map_ID = ?");
            ps.setInt(1, m.getSqlIndex());

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
            int mapId = 0;
            if(true)
            {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Map (MapName, `load`, SpawnClass, SpawnData) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, m.getName());
                ps.setString(2, m.getLoad());
                ps.setString(3, m.getSpawn().getClass().getName());
                ps.setString(4, m.getSpawn().getData());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                mapId =  rs.getInt(1);
                rs.close();

                ps.close();
            }
            if(true)
            {
                Practice.log(4,"new Map Id = " + mapId);
                Statement s = con.createStatement();
                for (MapComponent comp:m.getComponents())
                {
                    s.addBatch("INSERT INTO MapComponent (Map_ID, Class, Data) VALUES (" + mapId + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
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
            int kitId = -1;
            HashMap<Integer, HashMap<Integer, Integer>> orders = new HashMap<>();
            if (true)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Player WHERE Name = ? OR UUID = ?");
                ps.setString(1, p.getName());
                ps.setString(2, p.getUniqueId().toString());

                ResultSet res = ps.executeQuery();
                if (res.next())
                {
                    PlayerId = res.getInt("Player_ID");
                    kitId = res.getInt("Kit_ID");
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
                ps.setString(1, PlayerId + "");

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
            }
            Kit k = null;
            if(kitId != -1) k = getKit(kitId);
            Practice.log(4, "KitId = " + kitId + "," + (k == null));
            PracticePlayer pl = new PracticePlayer(PlayerId, p,orders, k);
            if(true){
                PreparedStatement ps = con.prepareStatement("SELECT * FROM PlayerComponent INNER JOIN Player ON Player.Player_ID = PlayerComponent.Player_ID WHERE PlayerComponent.Player_ID = ?");
                ps.setInt(1, PlayerId);

                ps.executeQuery();

                ResultSet res = ps.getResultSet();

                while (res.next())
                {
                    Class<?> clazz = Class.forName(res.getString("Class"));
                    Constructor<?> constructor = clazz.getConstructor(Player.class, String.class);
                    constructor.newInstance(pl, res.getString("Data"));
                }
            }
            return pl;
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
            PreparedStatement ps = con.prepareStatement("INSERT INTO Player (UUID, Name) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getUniqueId() + "");
            ps.setString(2, p.getName());
            
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
            PreparedStatement ps = con.prepareStatement("INSERT INTO KitOrder (`Order`, Kit_ID) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, new Gson().toJson(order));
            if(nullkitid) ps.setNull(2, Types.NULL);
            else ps.setInt(2, kitId);
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id =  rs.getInt(1);
            ps.close();
            
            ps = con.prepareStatement("INSERT INTO PlayerKitOrder (Player_ID, KitOrder_ID) VALUES (?,?)");
            ps.setInt(1, prac.getPlayerId());
            ps.setInt(2, id);
            
            ps.close();
            rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void updatePlayerKit(PracticePlayer player, Kit kit)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("UPDATE Player SET Player.Kit_ID = ? WHERE Player.Player_ID = ?");
            ps.setInt(1, kit.getSqlId());
            ps.setInt(2, player.getPlayerId());
            ps.executeUpdate();
            ps.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void saveComponents(PracticePlayer p)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM PlayerComponent INNER JOIN Player ON Player.Player_ID = PlayerComponent.Player_ID WHERE PlayerComponent.Player_ID = ?");
            ps.setInt(1, p.getPlayerId());

            ps.executeUpdate();
            ps.close();


            Statement s = con.createStatement();
            for (GameComponent comp : p.getComponents())
            {
                s.addBatch("INSERT INTO PlayerComponent (Player_ID, Class, Data) VALUES (" + p.getPlayerId() + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
            }
            s.executeBatch();
            s.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    public Kit getKit(int KitId)
    {
        return getKitFromQuery("SELECT * FROM Kit WHERE Kit.Kit_ID = " + KitId);
    }
    public Kit getKit(String name)
    {
        return getKitFromQuery("SELECT * FROM Kit WHERE Kit.Name = '" + name + "'");
    }
    private Kit getKitFromQuery(String query)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(query);

            ps.executeQuery();

            ResultSet res = ps.getResultSet();

            //new Instance to get List class
            ConfigItem[] items= null;
            int deforder = 0;
            String name = "";
            int kitId = 0;
            if(res.next())
            {
                java.util.Map<String, Object> map = new HashMap<>();
                items = new Gson().fromJson(res.getString("Items"), ConfigItem[].class);
                deforder = res.getInt("defaultOrder");
                name = res.getString("Name");
                kitId = res.getInt("Kit_ID");
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
            ps.setInt(1, deforder);

            ps.executeQuery();

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
                //add a new KitOrder with Order = '{}'
                ps = con.prepareStatement("INSERT INTO KitOrder (`Order`, Kit_ID) VALUES ('{}',?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, kitId);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id =  rs.getInt(1);
                ps.close();
                ps = con.prepareStatement("UPDATE Kit SET defaultOrder = ? WHERE Kit_ID = ?");
                ps.setInt(1, id);
                ps.setInt(2, kitId);
                ps.executeUpdate();
                ps.close();
            }

            ps.close();
            res.close();
            List<ItemStack> stacks = new ArrayList<>();
            for (ConfigItem i:items)
            {
                java.util.Map<String, Object> map = new HashMap<>();
                stacks.add(ItemStack.deserialize(new Gson().fromJson(i.stack, map.getClass())));
            }
            return new Kit(name, kitId, stacks, deforder, defaultOrder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    

    public void deleteKit(Kit k)
    {
        if(k.getSqlId() == -1) return;
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Kit WHERE Kit.Kit_ID = ?");
            ps.setInt(1, k.getSqlId());

            ps.executeUpdate();
            ps.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void addKit(Kit k)
    {
        if(k.getSqlId() != -1) return;
        try
        {
            int orderId = k.getDefaultOrderId();
            int KitId = 0;
            if(k.getDefaultOrderId() == -1)
            {
                PreparedStatement ps = con.prepareStatement("INSERT INTO KitOrder (`Order`, Kit_ID) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, k.getDefaultOrder());
                ps.setInt(2, -1);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                orderId =  rs.getInt(1);
                rs.close();

                ps.close();
            }
            if(true)
            {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Kit (Name, Items, defaultOrder) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, k.getName());
                ps.setString(2, k.getItems());
                ps.setInt(3, orderId);

                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                KitId =  rs.getInt(1);
                rs.close();

                ps.close();
            }
            if(true){
                Statement s = con.createStatement();
                for (KitComponent comp:k.getComponents())
                {
                    s.addBatch("INSERT INTO KitComponent (Kit_ID, Class, Data) VALUES (" + KitId + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
                }
                s.executeBatch();
                s.close();
            }
            if(true){
                //Update Kit_ID of default Order
                PreparedStatement ps = con.prepareStatement("UPDATE KitOrder SET Kit_ID = ? WHERE KitOrder_ID = ?");
                ps.setInt(1, KitId);
                ps.setInt(2, orderId);
                ps.executeUpdate();
                ps.close();
            }
            k.setSqlId(KitId);
            k.setDefaultOrderId(orderId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void updateKit(Kit kit)
    {
        try
        {
            String sql = "UPDATE KitOrder SET `Order` = '" + kit.getDefaultOrder() + "' WHERE KitOrder_ID = " + kit.getDefaultOrderId();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            //now update the kit parameter Items
            ps = con.prepareStatement("UPDATE Kit SET Items = ? WHERE Kit_ID = ?");
            ps.setString(1, kit.getItems());
            ps.setInt(2, kit.getSqlId());
            ps.executeUpdate();
            ps.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    public GameLogic getLogic(int logicId)
    {
        return getLogicFromQuery("SELECT * FROM Gamemode WHERE Gamemode.Gamemode_ID = " + logicId);
    }
    public GameLogic getLogic(String name)
    {
        return getLogicFromQuery("SELECT * FROM Gamemode WHERE Gamemode.Name = '" + name + "'");
    }
    private GameLogic getLogicFromQuery(String query){
        try
        {
            PreparedStatement ps = con.prepareStatement(query);

            ps.executeQuery();

            ResultSet res = ps.getResultSet();

            GameLogic logic = null;
            if(res.next())
            {
                Class clazz = Class.forName(res.getString("Class"));
                logic = (GameLogic) clazz.newInstance();
                logic.setName(res.getString("Name"));
                logic.setId(res.getInt("Gamemode_ID"));
                logic.applyData(res.getString("Data"));
            }

            ps.close();
            res.close();
            return logic;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public void deleteLogic(GameLogic logic)
    {
        if(logic.getId() == -1) return;
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Kit WHERE Kit.Kit_ID = ?");
            ps.setInt(1, logic.getId());

            ps.executeUpdate();
            ps.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void addLogic(GameLogic logic)
    {
        if(logic.getId() != -1) return;
        try
        {
            int logicId = 0;
            if(true)
            {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Gamemode (Name, Class, Data) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, logic.getName());
                ps.setString(2, logic.getClass().getName());
                ps.setString(3, logic.getData());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                logicId =  rs.getInt(1);
                rs.close();

                ps.close();
            }
            if(true){
                Statement s = con.createStatement();
                for (GameComponent comp:logic.getComponents())
                {
                    s.addBatch("INSERT INTO GameComponent (Gamemode_ID, Class, Data) VALUES (" + logicId + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
                }
                s.executeBatch();
                s.close();
            }
            logic.setId(logicId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    
    public void addStatsTable(GameLogic logic)
    {
        try
        {
            Statement statement = con.createStatement();
            statement.addBatch("CREATE TABLE IF NOT EXISTS `" + logic.getName() + "Stats` (" + logic.getName() + "Stats_ID` int(11) NOT NULL, `Player_ID` int(11) NOT NULL, CONSTRAINT `Player` FOREIGN KEY (`Player_ID`) REFERENCES `Player` (`Player_ID`) ON UPDATE NO ACTION ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=latin1");
            for (GameComponent component:logic.getComponents(IStatComponent.class))
            {
                IStatComponent comp = (IStatComponent) component;
                statement.addBatch("ALTER TABLE `" + logic.getName() + "Stats`\n" + "    ADD COLUMN IF NOT EXISTS `" + comp.getSQLName() + "` "+comp.getSQLType()+" NOT NULL;");
            }
            statement.executeBatch();
            statement.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void storeStats(GameLogic logic)
    {
        try
        {
            Statement statement = con.createStatement();
            for (Player p : logic.getPlayers())
            {
                String extraProperties = "";
                String extraValues = "";
                for (GameComponent component:logic.getComponents(IStatComponent.class))
                {
                    IStatComponent comp = (IStatComponent) component;
                    extraProperties += ",`" + comp.getSQLName() + "`";
                    extraValues += ",`" + comp.getStat(p) + "`";
                }
                String sql = "INSERT INTO `" + logic.getName() + "Stats` (`Player_ID`" + extraProperties + ") VALUES (`" + PracticePlayer.getPlayer(p).getPlayerId() + "`" + extraValues + ")";
                statement.addBatch(sql);
            }
            statement.executeBatch();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}