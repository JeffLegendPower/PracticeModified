package me.trixxtraxx.Practice.Lobby;

import me.TrixxTraxx.InventoryAPI.Items.BetterItem;
import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.Lobby.ItemTypes.*;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.SQL.PracticePlayer;
import me.trixxtraxx.Practice.Utils.ConfigLocation;
import me.trixxtraxx.Practice.libs.Fastboard.FastBoard;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

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
    private boolean blockHunger;
    private boolean blockWeather;
    private int voidOutHeight;
    private List<String> blockedInventories;
    private List<PracticePlayer> players = new List<>();
    private List<LobbyItem> items = new List<>();
    private ConfigLocation spawn;
    private List<FastBoard> scoreboards = new List<>();
    private List<Launchpad> launchpads = new List<>();
    
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
        this.blockHunger = section.getBoolean("BlockHunger");
        this.blockWeather = section.getBoolean("BlockWeather");
        this.voidOutHeight = section.getInt("VoidOutHeight");
        this.spawn = ConfigLocation.deserialize(section.getString("spawn"));
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
            else if("PARTIES".equals(Type))
            {
                items.add(new PartyItem(itemSec));
            }
            else if("EVENTS".equals(Type))
            {
                items.add(new EventItem(itemSec));
            }
        }
        ConfigurationSection launchpadSec = section.getConfigurationSection("launchpads");
        for(String key : launchpadSec.getKeys(false))
        {
            ConfigurationSection launchpad = launchpadSec.getConfigurationSection(key);
            launchpads.add(new Launchpad(launchpad, Bukkit.getWorld(world)));
        }
        lobbies.add(this);
    }
    
    public String getWorld(){return world;}
    public Location getSpawn(){
        return spawn.getLocation(Bukkit.getWorld(world));
    }
    public String getName(){return name;}
    public int getMaxRatedPlayers(){return maxRatedPlayers;}
    public boolean isBreakBlocked(){return blockBreak;}
    public boolean isPlaceBlocked(){return blockPlace;}
    public boolean isDamageBlocked(){return blockDamage;}
    public boolean isEntityDamageBlocked(){return blockEntityDamage;}
    public boolean isInvMoveBlocked(){return blockInvMove;}
    public boolean isDropBlocked(){return blockDrop;}
    public boolean isHungerBlocked(){return blockHunger;}
    public boolean isWeatherBlocked(){return blockWeather;}
    public int getVoidOutHeight(){return voidOutHeight;}
    public List<String> getBlockedInventories(){return blockedInventories;}
    public List<Launchpad> getLaunchpads(){return launchpads;}
    public LobbyItem getItem(int slot){return items.find(x -> x.getSlot() == slot);}
    public List<PracticePlayer> getPlayers(){return players;}
    public void addPlayer(PracticePlayer player){
        if(player == null || player.getPlayer() == null || players.contains(player)) return;
        Practice.log(4, "Adding player " + player.getName() + " to lobby " + name);
        players.add(player);
        new BukkitRunnable(){
            @Override
            public void run()
            {
                Player p = player.getPlayer();
                if(p == null) {
                    //had an exception so idk...
                    players.remove(player);
                    return;
                }
                p.setMaxHealth(20);
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setAllowFlight(false);
                p.setFireTicks(0);
                p.setNoDamageTicks(20);
                p.setGameMode(GameMode.SURVIVAL);
                p.setMaximumNoDamageTicks(20);
                p.teleport(spawn.getLocation(Bukkit.getWorld(world)));
                p.getActivePotionEffects().forEach(x -> p.removePotionEffect(x.getType()));
                
                setInv(player);
    
                FastBoard board = new FastBoard(player.getPlayer());
                board.updateTitle("§bPractice");
                board.updateLines(
                        "",
                        "§9Player: §b" + player.getName(),
                        "§9Global Elo: §b" + player.getGlobalElo(),
                        "",
                        "§bRanked.fun"
                );
            }
        }.runTaskLater(Practice.Instance, 1);
        
        BungeeUtil.getInstance().update();
    }
    public void removePlayer(PracticePlayer player, boolean update)
    {
        scoreboards.removeAll(x -> {
           if(x.getPlayer() == player){
               x.delete();
               return true;
           }
           return false;
        });
        if(players.remove(player))
            if(update)
                BungeeUtil.getInstance().update();
    }
    public void setInv(PracticePlayer player)
    {
        Player p = player.getPlayer();
        PlayerInventory inv = p.getInventory();
        inv.clear();
        if(!player.isInQueue())
        {
            inv.setArmorContents(null);
            for(LobbyItem item: items)
            {
                inv.setItem(item.getSlot(), item.getItem());
            }
        }
        else{
            p.getInventory().setItem(
                    8,
                    new BetterItem(Material.BARRIER).setDisplayName(ChatColor.RED + "Leave Queue")
            );
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