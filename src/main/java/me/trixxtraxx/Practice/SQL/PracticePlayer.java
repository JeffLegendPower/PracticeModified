package me.trixxtraxx.Practice.SQL;

import com.google.gson.Gson;
import me.TrixxTraxx.Linq.List;
import me.TrixxTraxx.RestCommunicator.PluginAPI.MessageProvider;
import me.trixxtraxx.Practice.Bungee.KitOrderUpdatePacket;
import me.trixxtraxx.Practice.Bungee.Queue.QueueUpdatePacket;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Lobby.Lobby;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PracticePlayer
{
    private static List<PracticePlayer> players = new List<>();
    private String playerName;
    private UUID uniqueId;
    private int playerId;
    private HashMap<Integer, HashMap<Integer, Integer>> customKitOrders;
    private List<PlayerStats> stats;
    private int kitId;
    private boolean queriedKit = false;
    private Kit cachedKit = null;
    private boolean inQueue = false;
    private Lobby lobby;
    
    public PracticePlayer(int playerId, int kitId, String playerName, UUID uniqueId, HashMap<Integer, HashMap<Integer, Integer>> customKitOrders, List<PlayerStats> stats, Lobby lobby)
    {
        this.playerId = playerId;
        this.playerName = playerName;
        this.uniqueId = uniqueId;
        this.customKitOrders = customKitOrders;
        this.stats = stats;
        this.kitId = kitId;
        this.lobby = lobby;
    }
    
    public void resetKit(){
        queriedKit = false;
        cachedKit = null;
    }
    public Player getPlayer(){return Bukkit.getPlayer(uniqueId);}
    public Kit getKit()
    {
        if(queriedKit) return cachedKit;
        if(kitId == -1){
            queriedKit = true;
            return cachedKit;
        }
        Kit kit = SQLUtil.Instance.getKit(kitId);
        queriedKit = true;
        cachedKit = kit;
        return kit;
    }
    public void createKit(List<ItemStack> items, HashMap<Integer, Integer> defaultOrder)
    {
        Kit kit = new Kit(playerName, -1, items, defaultOrder);
        kit.setNewItems(items);
        kit.setNewDefaultOrder(defaultOrder);
        cachedKit = kit;
        queriedKit = true;
        PracticePlayer finalPlayer = this;
        new BukkitRunnable(){
            @Override
            public void run(){
                SQLUtil.Instance.addKit(kit);
                SQLUtil.Instance.updatePlayerKit(finalPlayer, kit);
            }
        }.runTaskAsynchronously(Practice.Instance);
        return;
    }
    public int getPlayerId(){return playerId;}
    public HashMap<Integer, Integer> getCustomOrder(int kitId){return customKitOrders.get(kitId);}
    public void setCustomOrder(int kitId, HashMap<Integer, Integer> customOrder)
    {
        if(customKitOrders.containsKey(kitId))
        {
            Practice.log(4, "Updating custom order for kit " + kitId + " for player " + playerName);
            customKitOrders.remove(kitId);
            customKitOrders.put(kitId, customOrder);
            SQLUtil.Instance.updatePlayerKitOrder(this, kitId, customOrder);
        }
        else
        {
            Practice.log(4, "Adding custom order for kit " + kitId + " for player " + playerName);
            customKitOrders.put(kitId, customOrder);
            SQLUtil.Instance.addPlayerKitOrder(this, kitId, customOrder);
        }
        Practice.log(4, "now storing into bungee");
        MessageProvider.SendMessage("Practice_PlayerOrderUpdate", new Gson().toJson(new KitOrderUpdatePacket(getName(), kitId, customOrder)));
    }
    public void saveKit(){
        PlayerInventory inv = getPlayer().getInventory();
        HashMap<Integer, Integer> defaultOrder = new HashMap<>();
        List<ItemStack> items = new List<>();
        for(int i = 0; i < 40; i++)
        {
            ItemStack item = inv.getItem(i);
            if(item == null) continue;
            defaultOrder.put(items.size(), i);
            items.add(item);
        }
        boolean update = true;
        if(getKit() == null)
        {
            update = false;
            createKit(items, defaultOrder);
        }
        cachedKit.setNewItems(items);
        cachedKit.setNewDefaultOrder(defaultOrder);
        
        clearKitId(cachedKit.getSqlId());
    
        if(!update) return;
        PracticePlayer finalPlayer = this;
        new BukkitRunnable(){
            @Override
            public void run(){
                SQLUtil.Instance.updateKit(cachedKit);
                SQLUtil.Instance.updatePlayerKit(finalPlayer, cachedKit);
            }
        }.runTaskAsynchronously(Practice.Instance);
    }
    public void setInQueue(boolean inQueue)
    {
        if(this.inQueue != inQueue)
        {
            this.inQueue = inQueue;
            Practice.log(4, "Player " + playerName + " is now " + (inQueue ? "in" : "out") + " queue");
            lobby.setInv(this);
        }
    }
    public boolean isInQueue() {return inQueue;}
    public void leaveQueue()
    {
        setInQueue(false);
        MessageProvider.SendMessage("Practice_Queue_Update_Bungee", new Gson().toJson(new QueueUpdatePacket(getName(), false)));
    }
    
    //public double getGlobalElo()
    //{
    //    List<Double> elos = new List<>();
    //    for(PlayerStats stats:this.stats)
    //    {
    //        String elo = stats.getStat("Elo");
    //        if(elo != null && !elo.isEmpty() && elo.equalsIgnoreCase("null"))
    //        {
    //            try
    //            {
    //                elos.add(Double.parseDouble(elo));
    //            }
    //            catch(NumberFormatException e){}
    //        }
    //    }
    //    if(elos.size() == 0) return 0;
    //    return elos.avg(x -> x);
    //}
    
    public void openBungeeInventory(String inv){
        MessageProvider.SendMessage("PracticeGui", new Gson().toJson(new OpenGuiRequest(playerName, inv)));
    }
    public void executeBungeeCommand(String command){
        MessageProvider.SendMessage("ExecuteCommand", new Gson().toJson(new ExecuteCommand(playerName, command)));
    }
    public String getName(){
        return playerName;
    }
    
    //get the stats
    public List<PlayerStats> getStats(){return stats;}
    public PlayerStats getStats(String gm){return stats.find(x -> x.getGamemode()!= null && x.getGamemode().equalsIgnoreCase(gm));}
    
    private class OpenGuiRequest{
        private String player;
        private String gui;
        public OpenGuiRequest(String p, String inv)
        {
            player = p;
            gui = inv;
        }
    }
    private class ExecuteCommand{
        private String player;
        private String command;
        public ExecuteCommand(String p, String cmd)
        {
            player = p;
            command = cmd;
        }
    }
    
    public static PracticePlayer add(PracticePlayer p)
    {
        if(p.playerName == null || p.playerName.isEmpty()) return null;
        Practice.log(4, "Adding player: " + p.getName());
        players.add(p);
        return p;
    }
    public static PracticePlayer remove(PracticePlayer p){
        if(p != null)
        {
            Practice.log(4, "Removing player: " + p.getName());
            players.remove(p);
        }
        return p;
    }
    public static PracticePlayer getPlayer(Player p) {
        return players.find(x -> x.uniqueId.equals(p.getUniqueId()));
    }
    public static void clearKitId(int kitId)
    {
        players.forEach(p -> p.customKitOrders.remove(kitId));
        MessageProvider.SendMessage("Practice_KitOrderDelete", kitId + "");
    }
}
