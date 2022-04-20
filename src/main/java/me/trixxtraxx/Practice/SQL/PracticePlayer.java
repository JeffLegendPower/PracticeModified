package me.trixxtraxx.Practice.SQL;

import com.google.gson.Gson;
import me.TrixxTraxx.RestCommunicator.PluginAPI.MessageProvider;
import me.trixxtraxx.Practice.ComponentClass;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import me.TrixxTraxx.Linq.List;

public class PracticePlayer extends ComponentClass<PlayerComponent>
{
    private static List<PracticePlayer> players = new List<>();
    private Player player;
    private int playerId;
    private HashMap<Integer, HashMap<Integer, Integer>> customKitOrders;
    private Kit kit;
    
    public PracticePlayer(int playerId, Player p, HashMap<Integer, HashMap<Integer, Integer>> customKitOrders, Kit kit){
        player = p;
        this.customKitOrders = customKitOrders;
        this.playerId = playerId;
        this.kit = kit;
    }
    
    public Player getPlayer(){return player;}
    public Kit getKit(){return kit;}
    public int getPlayerId(){return playerId;}
    public HashMap<Integer, Integer> getCustomOrder(int kitId){return customKitOrders.get(kitId);}
    public void saveKit(){
        PlayerInventory inv = player.getInventory();
        HashMap<Integer, Integer> defaultOrder = new HashMap<>();
        List<ItemStack> items = new List<>();
        for(int i = 0; i < 40; i++)
        {
            ItemStack item = inv.getItem(i);
            if(item == null) continue;
            defaultOrder.put(items.size(), i);
            items.add(item);
        }
        if(kit == null){
            kit = new Kit(player.getName(), -1, items, -1, defaultOrder);
            kit.setNewItems(items);
            kit.setNewDefaultOrder(defaultOrder);
            SQLUtil.Instance.addKit(kit);
            SQLUtil.Instance.updatePlayerKit(this, kit);
            return;
        }
        kit.setNewItems(items);
        kit.setNewDefaultOrder(defaultOrder);
        
        SQLUtil.Instance.updateKit(kit);
        SQLUtil.Instance.updatePlayerKit(this, kit);
    }
    public void openBungeeInventory(String inv){
        MessageProvider.SendMessage("PracticeGui", new Gson().toJson(new OpenGuiRequest(player, inv)));
    }
    public void executeBungeeCommand(String command){
        MessageProvider.SendMessage("ExecuteCommand", new Gson().toJson(new ExecuteCommand(player, command)));
    }
    public void toLobby(){
        //TODO: implement
    }
    
    private class OpenGuiRequest{
        private String player;
        private String gui;
        public OpenGuiRequest(Player p, String inv)
        {
            player = p.getName();
            gui = inv;
        }
    }
    private class ExecuteCommand{
        private String player;
        private String command;
        public ExecuteCommand(Player p, String cmd)
        {
            player = p.getName();
            command = cmd;
        }
    }
    
    public static PracticePlayer generatePlayer(Player p){
        PracticePlayer prac = SQLUtil.Instance.getPlayer(p);
        players.add(prac);
        return prac;
    }
    public static PracticePlayer removePlayer(Player p){
        PracticePlayer prac = getPlayer(p);
        players.remove(prac);
        return prac;
    }
    public static PracticePlayer getPlayer(Player p){
        for (PracticePlayer prac:players)
        {
            if(prac.player == p) return prac;
        }
        return null;
    }
}
