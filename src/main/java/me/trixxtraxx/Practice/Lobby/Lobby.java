package me.trixxtraxx.Practice.Lobby;

import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.Lobby.ItemTypes.*;
import me.trixxtraxx.Practice.Lobby.ItemTypes.MenuItem;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.PlayerInventory;

import me.TrixxTraxx.Linq.List;

public class Lobby
{
    private static List<Lobby> lobbies = new List<>();
    private String world;
    private String name;
    private int maxRatedPlayers;
    private boolean blockBreak;
    private boolean blockPlace;
    private boolean blockDamage;
    private boolean blockEntityDamage;
    private boolean blockInvMove;
    private boolean blockDrop;
    //TODO: FOOD,
    private List<String> blockedInventories;
    private List<PracticePlayer> players = new List<>();
    private List<LobbyItem> items = new List<>();
    
    public Lobby(ConfigurationSection section){
        this.world = section.getString("world");
        this.name = section.getString("name");
        this.maxRatedPlayers = section.getInt("maxRatedPlayers");
        this.blockBreak = section.getBoolean("BlockBreak");
        this.blockPlace = section.getBoolean("BlockPlace");
        this.blockDamage = section.getBoolean("BlockDamage");
        this.blockEntityDamage = section.getBoolean("BlockEntityDamage");
        this.blockedInventories = new List<>(section.getStringList("blockedInventories"));
        this.blockInvMove = section.getBoolean("BlockInvMove");
        this.blockDrop = section.getBoolean("BlockDrop");
        ConfigurationSection ItemSec = section.getConfigurationSection("Items");
        for(String key : ItemSec.getKeys(false))
        {
            ConfigurationSection itemSec = ItemSec.getConfigurationSection(key);
            String Type = itemSec.getString("Type");
            if("OPEN_MENU".equals(Type))
            {
                items.add(new MenuItem(itemSec));
            }
            else if("CUSTOM_GAMEMODE".equals(Type))
            {
                items.add(new CustomGamemodeItem(itemSec));
            }
            else if("STATS".equals(Type))
            {
                items.add(new StatsItem(itemSec));
            }
            else if("PARTIES".equals(Type))
            {
                items.add(new PartyItem(itemSec));
            }
            else if("EVENTS".equals(Type))
            {
                items.add(new EventItem(itemSec));
            }
        }
        lobbies.add(this);
    }
    
    public String getWorld(){return world;}
    public String getName(){return name;}
    public int getMaxRatedPlayers(){return maxRatedPlayers;}
    public boolean isBreakBlocked(){return blockBreak;}
    public boolean isPlaceBlocked(){return blockPlace;}
    public boolean isDamageBlocked(){return blockDamage;}
    public boolean isEntityDamageBlocked(){return blockEntityDamage;}
    public boolean isInvMoveBlocked(){return blockInvMove;}
    public boolean isDropBlocked(){return blockDrop;}
    public List<String> getBlockedInventories(){return blockedInventories;}
    public LobbyItem getItem(int slot){return items.find(x -> x.getSlot() == slot);}
    public List<PracticePlayer> getPlayers(){return players;}
    public void addPlayer(PracticePlayer player){
        players.add(player);
        setInv(player);
        BungeeUtil.getInstance().update();
    }
    public void removePlayer(PracticePlayer player){
        players.remove(player);
        BungeeUtil.getInstance().update();
    }
    private void setInv(PracticePlayer player){
        PlayerInventory inv = player.getPlayer().getInventory();
        inv.clear();
        for(LobbyItem item : items)
        {
            inv.setItem(item.getSlot(), item.getItem());
        }
    }
    
    public static List<Lobby> getLobbies(){return lobbies;}
    public static Lobby get(World world){
        for(Lobby lobby : lobbies){
            if(lobby.getWorld().equalsIgnoreCase(world.getName()))
            {
                return lobby;
            }
        }
        return null;
    }
}