package me.trixxtraxx.Practice.SQL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.trixxtraxx.Practice.Bungee.GameAddAction;
import me.trixxtraxx.Practice.Bungee.GlobalStatUpdateAction;
import me.trixxtraxx.Practice.Bungee.StatUpdatePacket;
import me.trixxtraxx.Practice.Component;
import me.trixxtraxx.Practice.GameLogic.Components.Components.InventoryView.InventoryView;
import me.trixxtraxx.Practice.GameLogic.Components.Components.InventoryView.JsonItemStack;
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
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.TrixxTraxx.Linq.List;

public class SQLUtil
{
    public static SQLUtil Instance = new SQLUtil();
    
    private SQLUtil(){}
    
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
    
        try
        {
            connect();
        }
        catch(SQLException e)
        {
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
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false",
                                              username,
                                              password
            );
        }
    }
    
    public void applyComponents(Kit k)
    {
        if(k == null){
            Practice.log(1, "Tried to apply components to null kit");
            return;
        }
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM KitComponent INNER JOIN Kit ON Kit.Kit_ID = KitComponent.Kit_ID WHERE KitComponent.Kit_ID = ?");
            ps.setInt(1, k.getSqlId());
            
            ps.executeQuery();
            
            ResultSet res = ps.getResultSet();
            
            while(res.next())
            {
                Class<?> clazz = Class.forName(res.getString("Class"));
                Constructor<?> constructor = clazz.getConstructor(Kit.class);
                Component comp = (Component) constructor.newInstance(k);
                comp.applyData(res.getString("Data"));
            }
            
            ps.close();
            res.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void applyComponents(GameLogic logic)
    {
        if(logic == null){
            Practice.log(1, "Tried to apply components to null logic");
            return;
        }
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM GameComponent INNER JOIN Gamemode ON GameComponent.Gamemode_ID = Gamemode.Gamemode_ID WHERE GameComponent.Gamemode_ID = ?");
            ps.setInt(1, logic.getId());
            
            ps.executeQuery();
            
            ResultSet res = ps.getResultSet();
            
            while(res.next())
            {
                Class<?> clazz = Class.forName(res.getString("Class"));
                Constructor<?> constructor = clazz.getConstructor(GameLogic.class);
                Component comp = (Component) constructor.newInstance(logic);
                comp.applyData(res.getString("Data"));
            }
            
            ps.close();
            res.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void applyComponents(Map m)
    {
        if(m == null){
            Practice.log(1, "Tried to apply components to null map");
            return;
        }
        try
        {
            Practice.log(4, "Applying components to map " + m.getName());
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM MapComponent INNER JOIN Map ON Map.Map_ID = MapComponent.Map_ID WHERE Map.Map_ID = ?");
            ps.setInt(1, m.getSqlIndex());
            
            ResultSet res = ps.executeQuery();
            while(res.next())
            {
                Class<?> clazz = Class.forName(res.getString("Class"));
                Constructor<?> constructor = clazz.getConstructor(Map.class);
                Component comp = (Component) constructor.newInstance(m);
                comp.applyData(res.getString("Data"));
            }
            ps.close();
            res.close();
        }
        catch(Exception e)
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
            if(res.next())
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
        catch(Exception e)
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
        catch(Exception e)
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
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Map (MapName, `load`, SpawnClass, SpawnData) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, m.getName());
                ps.setString(2, m.getLoad());
                ps.setString(3, m.getSpawn().getClass().getName());
                ps.setString(4, m.getSpawn().getData());
    
                ps.executeUpdate();
    
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                mapId = rs.getInt(1);
                rs.close();
    
                ps.close();
            }
            if(true)
            {
                Practice.log(4, "new Map Id = " + mapId);
                Statement s = con.createStatement();
                for(MapComponent comp: m.getComponents())
                {
                    s.addBatch("INSERT INTO MapComponent (Map_ID, Class, Data) VALUES (" + mapId + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
                }
                s.executeBatch();
                s.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    /*public PracticePlayer getPlayer(Player p)
    {
        try
        {
            int PlayerId = -1;
            int kitId = -1;
            HashMap<Integer, HashMap<Integer, Integer>> orders = new HashMap<>();
            if(true)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Player WHERE Name = ? OR UUID = ?");
                ps.setString(1, p.getName());
                ps.setString(2, p.getUniqueId().toString());
    
                ResultSet res = ps.executeQuery();
                if(res.next())
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
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM KitOrder INNER JOIN PlayerKitOrder ON KitOrder.KitOrder_ID = PlayerKitOrder.KitOrder_ID INNER JOIN Player ON PlayerKitOrder.Player_ID = Player.Player_ID WHERE Player.Player_ID = ?");
                ps.setString(1, PlayerId + "");
    
                ResultSet res = ps.executeQuery();
                Gson gson = new Gson();
                //new instance for the getClass(), didnt find another way
                HashMap<Integer, Integer> map = new HashMap<>();
                if(res.next())
                {
                    orders.put(res.getInt("Kit_ID"), gson.fromJson(res.getString("Order"), map.getClass()));
                }
    
                ps.close();
                res.close();
            }
            Kit k = null;
            if(kitId != -1) k = getKit(kitId);
            Practice.log(4, "KitId = " + kitId + "," + (k == null));
            PracticePlayer pl = new PracticePlayer(PlayerId, p, orders, k);
            if(true)
            {
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM PlayerComponent INNER JOIN Player ON Player.Player_ID = PlayerComponent.Player_ID WHERE PlayerComponent.Player_ID = ?");
                ps.setInt(1, PlayerId);
    
                ps.executeQuery();
    
                ResultSet res = ps.getResultSet();
    
                while(res.next())
                {
                    Class<?> clazz = Class.forName(res.getString("Class"));
                    Constructor<?> constructor = clazz.getConstructor(Player.class, String.class);
                    constructor.newInstance(pl, res.getString("Data"));
                }
            }
            return pl;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }*/
    
    private int createPlayer(Player p)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Player (UUID, Name) VALUES (?,?)",
                                                        Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, p.getUniqueId() + "");
            ps.setString(2, p.getName());
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            
            ps.close();
            rs.close();
            return id;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }
    
    public void addPlayerKitOrder(PracticePlayer prac, int kitId, HashMap<Integer, Integer> order)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO PlayerKitOrder " +
                            "(Player_ID, Kit_ID, `Order`) " +
                            "VALUES (?,?,?) "
            );
            ps.setInt(1, prac.getPlayerId());
            ps.setInt(2, kitId);
            ps.setString(3, new Gson().toJson(order));
    
            ps.executeUpdate();
            ps.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void updatePlayerKitOrder(PracticePlayer prac, int kitId, HashMap<Integer, Integer> order)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE PlayerKitOrder " +
                            "SET `Order` = ? " +
                            "WHERE Player_ID = ? AND Kit_ID = ?"
            );
            ps.setString(1, new Gson().toJson(order));
            ps.setInt(2, prac.getPlayerId());
            ps.setInt(3, kitId);
            
            ps.executeUpdate();
            ps.close();
        }
        catch(Exception e)
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
        catch(Exception e)
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
            HashMap<Integer, Integer> defaultOrder = new HashMap<>();
            List<ItemStack> items = null;
            String name = "";
            int kitId = 0;
            if(res.next())
            {
                java.util.Map<String, Object> map = new HashMap<>();
                String ItemString = res.getString("Items");
                ItemString = ItemString.substring(7, ItemString.length() - 3);
                items = new List( BetterItem.deserialize(ItemString));
                name = res.getString("Name");
                kitId = res.getInt("Kit_ID");
    
                defaultOrder = new Gson().fromJson(res.getString("defaultOrder"), defaultOrder.getClass());
            }
            else
            {
                ps.close();
                res.close();
                return null;
            }
    
            ps.close();
            res.close();
            return new Kit(name, kitId, items, defaultOrder);
        }
        catch(Exception e)
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void addKit(Kit k)
    {
        if(k.getSqlId() != -1) return;
        try
        {
            int KitId = 0;
            if(true)
            {
                PreparedStatement ps = con.prepareStatement("INSERT INTO Kit (Name, Items, defaultOrder) VALUES (?,?," +
                                                                    "?)",
                                                            Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, k.getName());
                ps.setString(2, k.getItems());
                ps.setString(3, k.getDefaultOrder());
    
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                KitId = rs.getInt(1);
                rs.close();
    
                ps.close();
            }
            if(true)
            {
                Statement s = con.createStatement();
                for(KitComponent comp: k.getComponents())
                {
                    s.addBatch("INSERT INTO KitComponent (Kit_ID, Class, Data) VALUES (" + KitId + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
                }
                s.executeBatch();
                s.close();
            }
            k.setSqlId(KitId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void updateKit(Kit kit)
    {
        try
        {
            String sql = "Update Kit SET Name = ?, Items = ?, defaultOrder = ? WHERE Kit_ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kit.getName());
            ps.setString(2, kit.getItems());
            ps.setString(3, kit.getDefaultOrder());
            ps.setInt(4, kit.getSqlId());
            ps.executeUpdate();
            ps.close();
            
            ps = con.prepareStatement("DELETE FROM PlayerKitOrder WHERE Kit_ID = ?");
            ps.setInt(1, kit.getSqlId());
            ps.executeUpdate();
            ps.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public GameLogic getLogic(int logicId, boolean ranked)
    {
        return getLogicFromQuery("SELECT * FROM Gamemode WHERE Gamemode.Gamemode_ID = " + logicId, ranked);
    }
    
    public GameLogic getLogic(String name, boolean ranked){return getLogicFromQuery("SELECT * FROM Gamemode WHERE Gamemode.Name = '" + name + "'", ranked);}
    
    private GameLogic getLogicFromQuery(String query, boolean ranked)
    {
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
                
                Practice.log(4, "Loaded gamemode " + logic.getName() + " ranked: " + ranked);
                if(ranked)
                {
                    logic.setName(logic.getName() + "_Ranked");
                    String rankedClassName = res.getString("RankedClass");
                    if(rankedClassName != null && !rankedClassName.isEmpty())
                    {
                        Class<?> rankedClass = Class.forName(rankedClassName);
                        Constructor<?> constructor = rankedClass.getConstructor(GameLogic.class);
                        Component comp = (Component) constructor.newInstance(logic);
                        comp.applyData(res.getString("Data"));
                    }
                }
            }
    
            ps.close();
            res.close();
            return logic;
        }
        catch(Exception e)
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
        catch(Exception e)
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
                PreparedStatement ps = con.prepareStatement("INSERT INTO Gamemode (Name, Class, Data) VALUES (?,?,?)",
                                                            Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, logic.getName());
                ps.setString(2, logic.getClass().getName());
                ps.setString(3, logic.getData());
    
                ps.executeUpdate();
    
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                logicId = rs.getInt(1);
                rs.close();
    
                ps.close();
            }
            if(true)
            {
                Statement s = con.createStatement();
                for(GameComponent comp: logic.getComponents())
                {
                    s.addBatch("INSERT INTO GameComponent (Gamemode_ID, Class, Data) VALUES (" + logicId + ",'" + comp.getClass().getName() + "','" + comp.getData() + "')");
                }
                s.executeBatch();
                s.close();
            }
            logic.setId(logicId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public void addStatsTable(GameLogic logic)
    {
        try
        {
            Statement statement = con.createStatement();
            statement.addBatch(
                    "CREATE TABLE IF NOT EXISTS `" + logic.getName() + "Stats` (" +
                            "  `" + logic.getName() + "Stats_ID` int(11) NOT NULL AUTO_INCREMENT," +
                            "  `Player_ID` int(11) NOT NULL,\n" +
                            "  PRIMARY KEY (`" + logic.getName() + "Stats_ID`),\n" +
                            "  KEY `Player` (`Player_ID`),\n" +
                            "  UNIQUE KEY `UniquePlayer` (`Player_ID`)," +
                            "  CONSTRAINT `" + logic.getName() + "Stats1` FOREIGN KEY (`Player_ID`) REFERENCES `Player` (`Player_ID`) ON DELETE CASCADE ON\n" +
                            "  UPDATE\n" +
                            "    NO ACTION\n" +
                            ") ENGINE = InnoDB AUTO_INCREMENT = 9 DEFAULT CHARSET = utf8mb4"
            );
            statement.addBatch(
                    "CREATE TABLE IF NOT EXISTS `" + logic.getName() + "Games` (" +
                            "  `" + logic.getName() + "Games_ID` int(11) NOT NULL AUTO_INCREMENT," +
                            "  `Player_ID` int(11) NOT NULL,\n" +
                            "  PRIMARY KEY (`" + logic.getName() + "Games_ID`),\n" +
                            "  CONSTRAINT `" + logic.getName() + "Games1` FOREIGN KEY (`Player_ID`) REFERENCES `Player` (`Player_ID`) ON DELETE CASCADE ON\n" +
                            "  UPDATE\n" +
                            "    NO ACTION\n" +
                            ") ENGINE = InnoDB AUTO_INCREMENT = 9 DEFAULT CHARSET = utf8mb4"
            );
    
            statement.executeBatch();
            statement.close();
            
            
            statement = con.createStatement();
            
            for(GameComponent component: logic.getComponents(IStatComponent.class))
            {
                IStatComponent comp = (IStatComponent) component;
                for(IStatComponent.SQLProperty sql: comp.getSQL())
                {
                    if(sql.isPerGame()) continue;
                    statement.addBatch("ALTER TABLE `" + logic.getName() + "Stats`\n" + "    ADD COLUMN IF NOT EXISTS `" + sql.getName() + "` " + sql.getType());
                }
            }
            for(GameComponent component: logic.getComponents(IStatComponent.class))
            {
                IStatComponent comp = (IStatComponent) component;
                for(IStatComponent.SQLProperty sql: comp.getSQL())
                {
                    if(!sql.isPerGame()) continue;
                    statement.addBatch("ALTER TABLE `" + logic.getName() + "Games`\n" + "    ADD COLUMN IF NOT EXISTS `" + sql.getName() + "` " + sql.getType());
                }
            }
            statement.executeBatch();
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void storeStats(GameLogic logic)
    {
        try
        {
            List<GlobalStatUpdateAction> statUpdate = new List();
            List<GameAddAction> gameAdd = new List();
            Statement statement = con.createStatement();
            for(Player p: logic.getPlayers())
            {
                String extraGameProperties = "";
                String extraGameValues = "";
                
                String globalProperties = "";
                String globalValues = "";
                
                String globalUpdate = "";
                
                List<PlayerStats.GamemodeStat> gameStats = new List();
                
                for(GameComponent component: logic.getComponents(IStatComponent.class))
                {
                    IStatComponent comp = (IStatComponent) component;
                    for(IStatComponent.SQLProperty sql: comp.getSQL())
                    {
                        if(sql.isPerGame())
                        {
                            String val = comp.getStat(p, sql.getName());
                            gameStats.add(new PlayerStats.GamemodeStat(sql.getName(), val));
                            extraGameProperties += ",`" + sql.getName() + "`";
                            extraGameValues += "," + val;
                        }
                        else
                        {
                            String val = comp.getStat(p, sql.getName());
                            statUpdate.add(new GlobalStatUpdateAction(p.getName(), logic.getName(), sql.getName(), val));
                            globalProperties += ",`" + sql.getName() + "`";
                            globalValues += "," + val;
                            if(!globalUpdate.isEmpty()) globalUpdate += ",";
                            globalUpdate += "`" + sql.getName() + "` = " + val;
                        }
                    }
                }
    
                gameAdd.add(new GameAddAction(p.getName(), logic.getName(), new PlayerStats.GamemodeGame(gameStats)));
                
                String Gamesql = "INSERT INTO `" + logic.getName() + "Games` (`Player_ID`" + extraGameProperties + ") VALUES (" + PracticePlayer.getPlayer(p).getPlayerId() + extraGameValues + ")";
                statement.addBatch(Gamesql);
                Practice.log(4,Gamesql);
                if(globalUpdate.isEmpty()) continue;
                String insertIfNotExists = "INSERT INTO `" + logic.getName() + "Stats` " +
                        "(`Player_ID`" + globalProperties + ") " +
                        "VALUES (" + PracticePlayer.getPlayer(p).getPlayerId() + globalValues + ") " +
                        "ON DUPLICATE KEY UPDATE " + globalUpdate;
                statement.addBatch(insertIfNotExists);
                Practice.log(4,insertIfNotExists);
            }
            statement.executeBatch();
            StatUpdatePacket pack = new StatUpdatePacket(gameAdd, statUpdate);
            pack.update();
            pack.send();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public int cacheView(InventoryView view){
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO `InventoryView` (Slots, Items) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, new Gson().toJson( view.slots));
            ps.setString(2, view.getItems());
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            view.id = id;
            return id;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    public void deleteOldViews(){
        try{
            PreparedStatement ps = con.prepareStatement("DELETE FROM `InventoryView` WHERE `created_at` < ?");
            ps.setLong(1, System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 7));
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public InventoryView getView(int id){
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `InventoryView` WHERE `InventoryView_ID` = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                InventoryView view = new InventoryView();
                view.id = id;
                view.slots = new Gson().fromJson(rs.getString("Slots"), new List<Integer>().getClass());
                List<String> items = new Gson().fromJson(rs.getString("Items"), new List<String>().getClass());
                view.items = new List<>();
                for(String item: items){
                    view.items.add(JsonItemStack.fromJson(item));
                }
                Practice.log(4, "Items: " + view.items.size());
                return view;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}