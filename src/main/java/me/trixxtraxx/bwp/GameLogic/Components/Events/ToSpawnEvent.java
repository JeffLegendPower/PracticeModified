package me.trixxtraxx.bwp.GameLogic.Components.Events;

import me.trixxtraxx.bwp.GameLogic.Components.GameEvent;
import me.trixxtraxx.bwp.Gamemode.Game;
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
