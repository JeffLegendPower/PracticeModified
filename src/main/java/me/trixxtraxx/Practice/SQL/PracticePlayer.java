package me.trixxtraxx.Practice.SQL;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Kit.Kit;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PracticePlayer
{
    private static List<PracticePlayer> players = new ArrayList<>();
    private Player player;
    private int playerId;
    private HashMap<Integer, HashMap<Integer, Integer>> customKitOrders;
    private Kit kit;

    public PracticePlayer(int playerId, Player p, HashMap<Integer, HashMap<Integer, Integer>> customKitOrders, Kit kit)
    {
        player = p;
        this.customKitOrders = customKitOrders;
        this.playerId = playerId;
        this.kit = kit;
    }

    public static PracticePlayer generatePlayer(Player p)
    {
        PracticePlayer prac = SQLUtil.Instance.getPlayer(p);
        players.add(prac);
        return prac;
    }

    public static PracticePlayer removePlayer(Player p)
    {
        PracticePlayer prac = getPlayer(p);
        players.remove(prac);
        return prac;
    }

    public static PracticePlayer getPlayer(Player p)
    {
        for (PracticePlayer prac:players)
        {
            if(prac.player == p) return prac;
        }
        return null;
    }

    public Kit getKit(){return kit;}
    public int getPlayerId(){return playerId;}
    public HashMap<Integer, Integer> getCustomOrder(int kitId){return customKitOrders.get(kitId);}

    
    protected List<GameComponent> components = new ArrayList<>();
    public List<GameComponent> getComponents()
    {
        return components;
    }
    public void addComponent(GameComponent comp)
    {
        components.add(comp);
    }
    public void removeComponent(GameComponent comp)
    {
        components.remove(comp);
    }
    public List<GameComponent> getComponents(Class<?> c)
    {
        List<GameComponent> comps = new ArrayList<>();
        for (GameComponent comp:components)
        {
            if(c.isInstance(comp)) comps.add(comp);
        }
        return comps;
    }
    
    
    public void saveKit()
    {
        PlayerInventory inv = player.getInventory();
        HashMap<Integer, Integer> defaultOrder = new HashMap<>();
        List<ItemStack> items = new ArrayList<>();
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
}
