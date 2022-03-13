package me.trixxtraxx.Practice.GameEvents.AllModes;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.Gamemode.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ToSpawnEvent extends GameEvent
{
    private Player player;
    private Location loc;
    public ToSpawnEvent(Player p, Location loc)
    {
        super(Game.getGame(p).getLogic());
        player = p;
        this.loc = loc;
    }

    public Player getPlayer(){return player;}
    public Location getLoc(){return loc;}
}
