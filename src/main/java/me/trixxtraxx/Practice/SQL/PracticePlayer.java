package me.trixxtraxx.Practice.SQL;

import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Practice;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PracticePlayer
{
    private static List<PracticePlayer> players = new ArrayList<>();
    private Player player;
    private int playerId;
    private HashMap<Integer, HashMap<Integer, Integer>> customKitOrders;

    public PracticePlayer(int playerId, Player p, HashMap<Integer, HashMap<Integer, Integer>> customKitOrders)
    {
        player = p;
        this.customKitOrders = customKitOrders;
        this.playerId = playerId;
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
}
