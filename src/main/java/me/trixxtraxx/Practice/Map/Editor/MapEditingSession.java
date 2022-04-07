package me.trixxtraxx.Practice.Map.Editor;

import me.trixxtraxx.Practice.Map.Map;
import me.trixxtraxx.Practice.Map.MapComponent;
import me.trixxtraxx.Practice.SQL.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    private ComponentInventoryState invState;
    private int invPage;
    
    private enum ComponentInventoryState
    {
        NONE,
        OVERVIEW,
        COMPONENT_LIST,
        COMPONENT_SETTINGS,
        SET_INT,
        SET_STRING,
        SET_BOOLEAN,
        SET_MATERIAL,
        SET_LOCATION,
        SET_REGION,
        SET_ITEMSTACK,
        
    }

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
        ConfigurationSection componentSection = conf.getConfigurationSection("MapEditor.Components");
        for (String key : componentSection.getKeys(false))
        {
            if(!componentSection.isConfigurationSection(key)) continue;
            ConfigurationSection section = componentSection.getConfigurationSection(key);
            try
            {
                componentItems.put(Class.forName(section.getString("ComponentClass")).asSubclass(MapComponent.class),
                                   section.getItemStack("Item"));
                componentClasses.add(Class.forName(section.getString("ComponentClass")).asSubclass(MapComponent.class));
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
    //Slots 10-44 excluding 18  19 27  28  36 37 the maps components, make components a Compass
    //Slot 45,36 arrows for pagnation
    //Slot 50 Green wool for adding a component
    //Slot 48 Red wool for removing a component
    //fill up every empty slot thats not for map components with gray glass

    public void openComponentGui()
    {
        Inventory inventory = Bukkit.createInventory(player, 54, "Components");
        int index = 0;
        for (int i = 10; i < 44; i++)
        {
            if (!(index >= map.getComponents().size()))
            {
                if (i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36) continue;
                inventory.setItem(i, componentItems.get(map.getComponents().get(index).getClass()));
            }
            index++;
        }
        for (int i = 0; i < 10; i++)
        {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
        }
        for (int i = 45; i < 54; i++)
        {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
        }
        //set gray glass at the excluded Component slots
        inventory.setItem(17, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
        inventory.setItem(18, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
        inventory.setItem(26, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
        inventory.setItem(27, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
        inventory.setItem(35, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
        
        inventory.setItem(4, Description);
        inventory.setItem(36, BackArrow);
        inventory.setItem(44, NextArrow);
        inventory.setItem(48, RemoveComponent);
        inventory.setItem(50, AddComponent);
        player.openInventory(inventory);
    }
}