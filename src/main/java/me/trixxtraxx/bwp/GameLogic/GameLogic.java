package me.trixxtraxx.bwp.GameLogic;

import me.trixxtraxx.bwp.GameLogic.Components.GameComponent;
import me.trixxtraxx.bwp.Gamemode.Game;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class GameLogic
{
    protected List<GameComponent> components = new ArrayList<>();

    public GameLogic() {}
    public GameLogic(List<GameComponent> comps)
    {
        components = comps;
    }

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


    public abstract void start(Game gm, List<Player> players);
    public abstract World getWorld();
    public abstract List<Player> getPlayers();
}
