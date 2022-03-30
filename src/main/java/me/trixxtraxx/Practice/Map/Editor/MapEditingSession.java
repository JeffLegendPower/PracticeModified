package me.trixxtraxx.Practice.Map.Editor;

import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapEditingSession
{
    private static List<MapEditingSession> sessions = new ArrayList<>();
    private static ItemStack Description;
    private static ItemStack BackArrow;
    private static ItemStack NextArrow;
    private static ItemStack RemoveComponent;
    private static ItemStack AddComponent;
    private static HashMap<Class<? extends MapComponent>, ItemStack> componentItems = new HashMap<>();
    private static List<Class<? extends MapComponent>> componentClasses = new ArrayList<>();
    private Player player;
    private Map map;
    private World world;

    public MapEditingSession(Player player, Map m)
    {
        this.player = player;
        this.map = m;
        world = m.load();
        player.teleport(world.getSpawnLocation());
    }

    //load all the stuff from the config from Path "MapEditor."
    public static void init(FileConfiguration conf){
        Description = conf.getItemStack("MapEditor.DescriptionItem");
        BackArrow = conf.getItemStack("MapEditor.BackArrowItem");
        NextArrow = conf.getItemStack("MapEditor.NextArrowItem");
        RemoveComponent = conf.getItemStack("MapEditor.RemoveComponentItem");
        AddComponent = conf.getItemStack("MapEditor.AddComponentItem");
        for (ConfigurationSection section : conf.getConfigurationSection("MapEditor.Components").getKeys(false))
        {
            try
            {
                componentItems.put(Class.forName(key).asSubclass(MapComponent.class), conf.getItemStack("MapEditor.Components." + key));
                componentClasses.add(Class.forName(key).asSubclass(MapComponent.class));
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void addSession(MapEditingSession session) {sessions.add(session);}
    public static void removeSession(MapEditingSession session) {sessions.remove(session);}
    public static MapEditingSession getSession(Player player)
    {
        for (MapEditingSession session : sessions)
        {
            if (session.getPlayer() == player)
            {
                return session;
            }
        }
        return null;
    }
    public static List<MapEditingSession> getSessions() {return sessions;}

    public Player getPlayer() {return player;}
    public Map getMap() {return map;}

    public World getWorld() {return world;}

    public void exit(boolean save)
    {
        map.unload(true);
        if (save)
        {
            SQLUtil.Instance.deleteMap(map);
            SQLUtil.Instance.addMap(map);
        }
        //tp to lobby
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        removeSession(this);
    }

    //Create a inventory with the following items:
    //Slot 4 = Book that has a title of "What is This" and a lore of "Components are a important part of a map!"
    //Slots 10,11,12,13,14,15,16,17,20,21,22,23,24,25,26,29,30,31,32,33,34,35, 38,39,40,41,42,43,44 the maps components, make components a Compass
    //Slot 45,36 arrows for pagnation
    //Slot 50 Green wool for adding a component
    //Slot 48 Red wool for removing a component

    public void openComponentGui()
    {
        Inventory inventory = Bukkit.createInventory(player, 54, "Components");
        inventory.setItem(4, );
        for (int i = 10; i < 45; i++)
        {
            if(i == 18 || i == 19 || i == 27 || i == 28 || i == 36 || i == 37) continue;
            inventory.setItem(i, );
        }
        inventory.setItem(45, );
        inventory.setItem(36, );
        inventory.setItem(50, );
        inventory.setItem(48, );
        player.openInventory(inventory);
    }
}