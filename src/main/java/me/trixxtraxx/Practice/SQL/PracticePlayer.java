package me.trixxtraxx.Practice.SQL;

import com.google.gson.Gson;
import me.TrixxTraxx.RestCommunicator.PluginAPI.MessageProvider;
import me.trixxtraxx.Practice.Bungee.BungeeUtil;
import me.trixxtraxx.Practice.ComponentClass;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import me.TrixxTraxx.Linq.List;

public class PracticePlayer
{
    private static List<PracticePlayer> players = new List<>();
    private String playerName;
    private int playerId;
    private HashMap<Integer, HashMap<Integer, Integer>> customKitOrders;
    private List<PlayerStats> stats;
    private int kitId;
    private boolean queriedKit = false;
    private Kit cachedKit = null;
    
    public PracticePlayer(int playerId, int kitId, String playerName, HashMap<Integer, HashMap<Integer, Integer>> customKitOrders, List<PlayerStats> stats)
    {
        this.playerName = playerName;
        this.customKitOrders = customKitOrders;
        this.playerId = playerId;
        this.stats = stats;
        this.kitId = kitId;
    }
    
    public void resetKit(){
        queriedKit = false;
        cachedKit = null;
    }
    public Player getPlayer(){return Bukkit.getPlayer(playerName);}
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
        Kit kit = new Kit(playerName, -1, items, -1, defaultOrder);
        kit.setNewItems(items);
        kit.setNewDefaultOrder(defaultOrder);
        SQLUtil.Instance.addKit(kit);
        SQLUtil.Instance.updatePlayerKit(this, kit);
        return;
    }
    public int getPlayerId(){return playerId;}
    public HashMap<Integer, Integer> getCustomOrder(int kitId){return customKitOrders.get(kitId);}
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
        if(getKit() == null)
        {
            createKit(items, defaultOrder);
        }
        cachedKit.setNewItems(items);
        cachedKit.setNewDefaultOrder(defaultOrder);
        
        SQLUtil.Instance.updateKit(cachedKit);
        SQLUtil.Instance.updatePlayerKit(this, cachedKit);
    }
    public void openBungeeInventory(String inv){
        MessageProvider.SendMessage("PracticeGui", new Gson().toJson(new OpenGuiRequest(playerName, inv)));
    }
    public void executeBungeeCommand(String command){
        MessageProvider.SendMessage("ExecuteCommand", new Gson().toJson(new ExecuteCommand(playerName, command)));
    }
    public void toLobby(){
        BungeeUtil.getInstance().toLobby(getPlayer());
    }
    public String getName(){
        return playerName;
    }
    
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
        Practice.log(4, "Adding player: " + p.getName());
        players.add(p);
        return p;
    }
    public static PracticePlayer remove(PracticePlayer p){
        Practice.log(4, "Removing player: " + p.getName());
        players.remove(p);
        return p;
    }
    public static PracticePlayer getPlayer(Player p){
        for (PracticePlayer prac:players)
        {
            if(prac.playerName.equalsIgnoreCase(p.getName())) return prac;
        }
        return null;
    }
}
