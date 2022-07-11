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
    private World world;
    private String name;
    private int maxRatedPlayers;
    private boolean canPlaceBlocks;
    private boolean canBreakBlocks;
    private boolean canDamageBlocks;
    private boolean allowEntityDamage;
    private boolean allowEditingInventory;
    private boolean canDropItems;
    private boolean lockHunger;
    private boolean lockWeather;
    private int voidOutHeight;
    private List<String> blockedInventories;
    private List<PracticePlayer> players = new List<>();
    private List<LobbyItem> items = new List<>();
    private ConfigLocation spawn;
    private List<FastBoard> scoreboards = new List<>();
    private List<Launchpad> launchpads = new List<>();

    public Lobby(
            World world,
            String name,
            int maxRatedPlayers,
            boolean canPlaceBlocks,
            boolean canBreakBlocks,
            boolean canDamageBlocks,
            boolean allowEntityDamage,
            boolean allowEditingInventory,
            boolean canDropItems,
            boolean lockHunger,
            boolean lockWeather,
            int voidOutHeight,
            List<String> blockedInventories,
            List<LobbyItem> items,
            List<Launchpad> launchpads,
            ConfigLocation spawn
    ) {
        this.world = world;
        this.name = name;
        this.maxRatedPlayers = maxRatedPlayers;
        this.canPlaceBlocks = canPlaceBlocks;
        this.canBreakBlocks = canBreakBlocks;
        this.canDamageBlocks = canDamageBlocks;
        this.allowEntityDamage = allowEntityDamage;
        this.allowEditingInventory = allowEditingInventory;
        this.canDropItems = canDropItems;
        this.lockHunger = lockHunger;
        this.lockWeather = lockWeather;
        this.voidOutHeight = voidOutHeight;
        this.blockedInventories = blockedInventories;
        this.items = items;
        this.launchpads = launchpads;
        this.spawn = spawn;

        lobbies.add(this);
    }

    public World getWorld() {return world;}
    public Location getSpawn() {
        return spawn.getLocation(world);
    }
    public String getName(){return name;}
    public int getMaxRatedPlayers(){return maxRatedPlayers;}
    public boolean getCanPlaceBlocks(){return canPlaceBlocks;}
    public boolean getCanBreakBlocks(){return canBreakBlocks;}
    public boolean getCanDamageBlocks(){return canDamageBlocks;}
    public boolean getAllowEntityDamage(){return allowEntityDamage;}
    public boolean getAllowEditingInventory(){return allowEditingInventory;}
    public boolean getCanDropItems(){return canDropItems;}
    public boolean getLockHunger(){return lockHunger;}
    public boolean getLockWeather(){return lockWeather;}
    public int getVoidOutHeight(){return voidOutHeight;}
    public List<String> getBlockedInventories(){return blockedInventories;}
    public List<Launchpad> getLaunchpads(){return launchpads;}
    public LobbyItem getItem(int slot){return items.find(x -> x.getSlot() == slot);}
    public List<LobbyItem> getItems(){return items;}
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
                p.teleport(spawn.getLocation(world));
                p.getActivePotionEffects().forEach(x -> p.removePotionEffect(x.getType()));

                setInv(player);

                FastBoard board = new FastBoard(player.getPlayer());
                board.updateTitle("§bPractice");
                board.updateLines(
                        "",
                        "§9Player: §b" + player.getName(),
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
}