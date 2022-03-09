package me.trixxtraxx.Practice.GameEvents.AllModes;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.Gamemode.Game;
import org.bukkit.entity.Player;

public class ToSpawnEvent extends GameEvent
{
    private Player player;
    public ToSpawnEvent(Player p)
    {
        super(Game.getGame(p).getLogic());
        player = p;
    }

    public Player getPlayer(){return player;}
}
